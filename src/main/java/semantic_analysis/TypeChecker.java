package semantic_analysis;

import ast.*;
import support.Errors;
import support.Pair;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//ToDo: should be changed to VisitorBase when finished
public class TypeChecker extends VisitorDefault<Optional<ast.Type>> {
    private final SymbolTable symbolTable;
    private final VisitedBlocks visitedBlocks;
    private final Errors errors;

    public VisitedBlocks getVisitedBlocks() {
        return visitedBlocks;
    }

    public TypeChecker(SymbolTable symbolTable) {
        super(Optional.empty());
        this.symbolTable = symbolTable;
        this.visitedBlocks = new VisitedBlocks();
        this.errors = new Errors();
    }

    public Errors getErrors() {
        return this.errors;
    }

    @Override
    public Optional<Type> visit(ExpUnaryOperation operation) {
        // Fetch the method signature
        Signature testedSignature = Signatures.unary.get(operation.getOperator());
        // Get the type of the expression
        Optional<Type> expressionType = operation.getExpression().accept(this);

        assert expressionType.isPresent();

        // If the expression type dosen't correspond to the signature, error
        if (!testedSignature.check(expressionType.get())) {
            this.errors.add("At " + operation.getPosition() + ": argument has type "
                    + expressionType.get() + " but type " + testedSignature.getArgumentsTypes().get(0)
                    + " is expected.");
        }
        // Return the return type of the operation
        return testedSignature.getReturnType();
    }

    @Override
    public Optional<Type> visit(ExpBinaryOperation operation) {
        // Get the type of the left expression
        Optional<Type> left = operation.getLeft().accept(this);
        // Get the type of the right expression
        Optional<Type> right = operation.getRight().accept(this);

        assert left.isPresent();
        assert right.isPresent();

        // EQ and DIFF are polymorphic
        if (operation.getOperator().equals(EnumBinaryOp.EQ) || operation.getOperator().equals(EnumBinaryOp.DIFF)) {
            // Both side must have the same type to compare
            if (left != right)
                errors.add("Expression at " + operation.getLeft().getPosition()
                        + " should have the same type "
                        + "than expression at "
                        + operation.getRight().getPosition() + ".");
            // Return the return type of the operation
            return Optional.of(Signature.Bool);
        } else {
            // Fetch the method signature
            Signature testedSignature = Signatures.binary.get(operation.getOperator());
            // If the expression type dosen't correspond to the signature, error
            if (!testedSignature.check(left.get(), right.get())) {
                this.errors.add("At " + operation.getPosition() + ": arguments have types "
                        + left + " and " + right + " but types "
                        + testedSignature.getArgumentsTypes().get(0) + " and "
                        + testedSignature.getArgumentsTypes().get(1)
                        + " are expected.");
            }
            // Return the return type of the operation
            return testedSignature.getReturnType();
        }
    }

    @Override
    public Optional<Type> visit(ExpBoolean bool) {
        return Optional.of(Signature.Bool);
    }

    @Override
    public Optional<Type> visit(ExpCharacter character) {
        return Optional.of(Signature.Char);
    }

    @Override
    public Optional<Type> visit(ExpInteger integer) {
        return Optional.of(Signature.Integer);
    }

    @Override
    public Optional<Type> visit(ExpVariable variable) {
        // Return the return type of the variable in the symbol table (if present)
        return this.symbolTable.variableLookup(variable.getVariable(), this.visitedBlocks);
    }

    // FIXME
    // Can't find how to recover "in a clean way" the name of the record
    @Override
    public Optional<Type> visit(ExpRecordAccess record) {
        // Get the type of the record
        Optional<Type> recordType = record.getRecord().accept(this);
        assert recordType.isPresent();
        TypVariable recordRealType = (TypVariable) recordType.get();
        
        // Get all the fields of the given record
        Optional<List<Pair<String, Type>>> recordFields = this.symbolTable.typeDefinitionLookup(recordRealType.getName());
        assert recordFields.isPresent();

        // Try to find the corresponding field
        Optional<Pair<String, Type>> correspondingRecordField = recordFields.get().stream().filter(pair -> pair.getFst().equals(record.getField())).findFirst();
        // If no fields correspond, error
        if (correspondingRecordField.isEmpty()) {
            this.errors.add("At " + record.getPosition() + ": Can't find field "
                    + record.getField() + " inside record "
                    + recordRealType.getName());
            return defaultValue;
        }
        // Else, return the type of the corresponding field
        return Optional.of(correspondingRecordField.get().getSnd());
    }

    @Override
    public Optional<Type> visit(ExpArrayAccess array) {
        // Get the type of the index
        Optional<Type> indexType = array.getIndex().accept(this);
        assert indexType.isPresent();
        
        // if the index is not an integer, error
        if (!indexType.get().equals(Signature.Integer)) {
            this.errors.add("At " + array.getPosition() + ": index has type "
                    + indexType.get() + " but type "
                    + Signature.Integer
                    + " is expected.");
        }
        // Return the type of the accessed element, which is the type of the array
        return array.getArray().accept(this);
    }

    @Override
    public Optional<Type> visit(ExpArrayEnum enumeration) {
        // If the enum is empty, return nothing
        if (enumeration.getElements().size() == 0) return this.defaultValue;
        // If the enum isn't empty, get the type of the first element of the enum
        Optional<Type> firstType = enumeration.getElements().get(0).accept(this);
        assert firstType.isPresent();
        
        // Check that all elements of the enum have the same type
        for (Expression exp : enumeration.getElements()) {
            // Get the type of the current element
            Optional<Type> currentType = exp.accept(this);
            assert currentType.isPresent();
            // If the types differ, error
            if (!firstType.get().equals(currentType.get())) {
                this.errors.add("At " + enumeration.getPosition() + ": arguments have different types ("
                        + firstType.get() + " and " + currentType.get() + ")");
            }
        }
        
        // Return the type of the first element (which is supposed to be the type of all elements)
        return firstType;
    }

    @Override
    public Optional<Type> visit(ExpRecordEnum enumeration) {
        // Get the list of all the fields in the enum, and look for a record that has the same fields names
        Optional<Pair<String, List<Pair<String, Type>>>> optionalType = this.symbolTable.typeNameLookup(enumeration.getFieldValues().stream().map(Pair::getFst).collect(Collectors.toList()));

        // If no matching record is found, error
        if (optionalType.isEmpty()) {
            this.errors.add("At " + enumeration.getPosition() + ": No record were found matching the following fields ("
                    + enumeration.getFieldValues().stream().map(Pair::getFst).collect(Collectors.joining(", ")) + ")");
        }

        // return nothing (records don't really have type) 
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(ExpFunctionCall function) {

        // Special cases for free() and length() (polymorphic)
        // FIXME get rid of instanceof
        if (function.getName().equals(EnumPredefinedOp.FREE.toString())) {
            // If more than one parameter given, error
            if (function.getArguments().size() != 1)
                this.errors.add("At " + function.getPosition() + ": Free() method takes exactly one argument");
            else {
                // Get the type of the argument
                Optional<Type> argumentType = function.getArguments().get(0).accept(this);
                assert argumentType.isPresent();
                // If the argument isn't an array, error
                if (!(argumentType.get() instanceof TypArray))
                    this.errors.add("At " + function.getPosition() + ": Free() expects type Array, but " + argumentType.get() + " given");
                // Free dosen't return anything
                return Optional.of(Signature.Void);
            }
        } else if (function.getName().equals(EnumPredefinedOp.LENGTH.toString())) {
            // If more than one parameter given, error
            if (function.getArguments().size() != 1)
                this.errors.add("At " + function.getPosition() + ": Length() method takes exactly one argument");
            else {
                Optional<Type> argumentType = function.getArguments().get(0).accept(this);
                assert argumentType.isPresent();
                // If the argument isn't an array, error
                if (!(argumentType.get() instanceof TypArray))
                    this.errors.add("At " + function.getPosition() + ": Length() expects type Array, but " + argumentType.get() + " given");
                return Optional.of(Signature.Integer);
            }
        }

        // Get the signatures corresponding to the given name
        List<Signature> signatures = this.symbolTable.functionLookup(function.getName());
        // Get the list of arguments types in order
        List<Type> expCallArgumentsType = new ArrayList<>();
        for (Expression exp : function.getArguments())
            exp.accept(this).ifPresent(expCallArgumentsType::add);
        
        // Check if a signature correspond
        Optional<Signature> correspondingSignature = signatures.stream().filter((Signature sig) -> sig.check(expCallArgumentsType)).findFirst();
        // If none correspond, error
        if (correspondingSignature.isEmpty()) {
            this.errors.add("At " + function.getPosition() + ": No matching function found with arguments type ("
                    + expCallArgumentsType.stream().map(Type::toString).collect(Collectors.joining(", ")) + ")");
            return defaultValue;
        }
        // Return the corresponding signature return type
        else return correspondingSignature.get().getReturnType();
//        for (Signature currentSignature : signatures) {
//            if (currentSignature.check(expCallArgumentsType)) {
//                return currentSignature.getReturnType();
//            }
//        }
//        this.errors.add("At " + function.getPosition() + ": No matching function found with arguments type ("
//                + expCallArgumentsType.stream().map(Type::toString).collect(Collectors.joining(", ")) + ")");
//        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(ExpString string) {
        return Optional.of(Signature.String);
    }

    @Override
    public Optional<Type> visit(ExpNew expression) {
        // Get the type of the argument
        Optional<Type> argumentType = expression.getArgument().accept(this);
        assert argumentType.isPresent();

        // If the type of the expression dosen't match the type of the argument, error
        if (!expression.getType().equals(argumentType.get())) {
            this.errors.add("At " + expression.getPosition() + ": Argument has type "
                    + argumentType.get() + ", but type "
                    + expression.getType() + " was expected");
        }
        // Return the type of the expression
        return Optional.of(expression.getType());
    }

    @Override
    public Optional<Type> visit(TypPrimitive typPrimitive) {
        switch (typPrimitive.getType()) {
            case INT -> { return Optional.of(Signature.Integer); }
            case CHAR -> { return Optional.of(Signature.Char); }
            case BOOL -> { return Optional.of(Signature.Bool); }
            default -> throw new RuntimeException("Unexpected TypPrimitive");
        }
    }

    @Override
    public Optional<Type> visit(TypDico typDictionary) {
        // Return the type of the elements stored in the dictionary
        return Optional.of(typDictionary.getType());
    }

    @Override
    public Optional<Type> visit(TypVariable typRecord) {
        // return the TypVariable corresponding to the type of the record
        return Optional.of(typRecord);
    }

    @Override
    public Optional<Type> visit(TypArray typArray) {
        // Return the type of the elements stored in the array
        return Optional.of(typArray.getType());
    }

    // TODO check if return type match function return type
    @Override
    public Optional<Type> visit(Function function) {
        // Type checking on parameters
        function.getParameters().forEach(decl -> decl.accept(this));

        // Type checking on the function body, and optional return type
        Optional<Type> returnType = function.getBody().accept(this);
        // If no return type was expected, error
        if (returnType.isPresent() && function.getReturn_type().isEmpty())
            this.errors.add("At " + function.getPosition() + ": No return expected, but given "
                    + returnType.get());
        // If no return type was given, but one was expected, error
        else if (returnType.isEmpty() && function.getReturn_type().isPresent())
            this.errors.add("At " + function.getPosition() + ": Expected return type "
                    + function.getReturn_type().get() + ", but none given");
        // If given return type mismatch expected return type, error
        else if (returnType.isPresent() && function.getReturn_type().isPresent() && !returnType.equals(function.getReturn_type())) {
            this.errors.add("At " + function.getPosition() + ": Expected return type "
                    + function.getReturn_type().get() + ", but type "
                    + returnType.get() + " given");
        }

        // Result is never used
        return function.getReturn_type();
    }

    @Override
    public Optional<Type> visit(TypeDefinition typeDefinition) {
        // type checking on declarations
        typeDefinition.getDeclarations().forEach(decl -> decl.accept(this));

        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(Import imports) {
        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(GlobalDeclaration globalDeclaration) {
        // Check if an expression is given, and if it corresponds to the declaration's type
        if (globalDeclaration.getExpression().isPresent()) {
            Optional<Type> expressionType = globalDeclaration.getExpression().get().accept(this);
            assert expressionType.isPresent();
            if (!expressionType.get().equals(globalDeclaration.getType()))
                this.errors.add("At " + globalDeclaration.getPosition() + ": Tried to assign "
                        + expressionType.get() + " to global declaration of type "
                        + globalDeclaration.getType());
        }
        // Result is never used
        return Optional.of(globalDeclaration.getType());
    }

    @Override
    public Optional<Type> visit(Program program) {
        // Visit all program
        program.getImports().forEach(imp -> imp.accept(this));
        program.getGlobalDeclarations().forEach(glob -> glob.accept(this));
        program.getTypeDefinitions().forEach(typ -> typ.accept(this));
        program.getFunctions().forEach(func -> func.accept(this));
        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(Declaration declaration) {
        // Check if an expression is given, and if it corresponds to the declaration's type
        if (declaration.getExpression().isPresent()) {
            Optional<Type> expressionType = declaration.getExpression().get().accept(this);
            assert expressionType.isPresent();
            if (!expressionType.get().equals(declaration.getType())) {
                this.errors.add("At " + declaration.getPosition() + ": Tried to assign "
                        + expressionType.get() + " to declaration of type "
                        + declaration.getType());
            }
        }
        return Optional.of(declaration.getType());
    }

    @Override
    public Optional<Type> visit(InsFor instruction) {

        // Check if the declared iterator is an Integer
        Optional<Type> declarationType = instruction.getDeclaration().accept(this);
        assert declarationType.isPresent();
        if (!declarationType.get().equals(Signature.Integer)) {
            this.errors.add("At " + instruction.getPosition() + ": iterator declaration is of type "
                    + declarationType.get() + ", but type "
                    + Signature.Integer + " expected");
        }

        // Check if the condition is an Boolean
        Optional<Type> rangeType = instruction.getRange().accept(this);
        assert rangeType.isPresent();
        if (!rangeType.get().equals(Signature.Bool)) {
            this.errors.add("At " + instruction.getPosition() + ": range condition is of type "
                    + rangeType.get() + ", but type "
                    + Signature.Bool + " expected");
        }

        // Check if the step is an Integer
        Optional<Type> stepType = instruction.getStep().accept(this);
        assert stepType.isPresent();
        if (!stepType.get().equals(Signature.Integer)) {
            this.errors.add("At " + instruction.getPosition() + ": step is of type "
                    + stepType.get() + ", but type "
                    + Signature.Integer + " expected");
        }

        Optional<Type> returnType = instruction.getBody().accept(this);
        // If a return type was given, error
        returnType.ifPresent(type -> this.errors.add("At " + instruction.getPosition() + ": No return expected, but given " + type));

        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InstForeach instruction) {
        // Get the type of the declared placeholder
        Optional<Type> declarationType = instruction.getDeclaration().accept(this);
        assert declarationType.isPresent();
        // Get the type of the elements inside the collection
        Optional<Type> collectionType = instruction.getCollection().accept(this);
        assert collectionType.isPresent();

        // If the placeholder dosen't have the same type as the elements inside the collection, error
        if (!declarationType.get().equals(collectionType.get())) {
            this.errors.add("At " + instruction.getPosition() + ": declaration of type "
                    + declarationType.get() + ", but collection of type "
                    + collectionType.get());
        }

        Optional<Type> returnType = instruction.getBody().accept(this);
        // If a return type was given, error
        returnType.ifPresent(type -> this.errors.add("At " + instruction.getPosition() + ": No return expected, but given " + type));

        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InsWhile instruction) {
        // Check if the condition is an Boolean
        Optional<Type> conditionType = instruction.getCondition().accept(this);
        assert conditionType.isPresent();
        if (!conditionType.get().equals(Signature.Bool)) {
            this.errors.add("At " + instruction.getPosition() + ": condition is of type "
                    + conditionType.get() + ", but type "
                    + Signature.Bool + " expected");
        }

        Optional<Type> returnType = instruction.getBody().accept(this);
        // If a return type was given, error
        returnType.ifPresent(type -> this.errors.add("At " + instruction.getPosition() + ": No return expected, but given " + type));

        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InsIf instruction) {
        // Check if the condition is an Boolean
        Optional<Type> conditionType = instruction.getCondition().accept(this);
        assert conditionType.isPresent();
        if (!conditionType.get().equals(Signature.Bool))
            this.errors.add("At " + instruction.getPosition() + ": condition is of type "
                    + conditionType.get() + ", but type "
                    + Signature.Bool + " expected");

        Optional<Type> returnType = instruction.getBody().accept(this);
        // If a return type was given, error
        returnType.ifPresent(type -> this.errors.add("At " + instruction.getPosition() + ": No return expected, but given " + type));

        // Recursive call for all the ElseIf
        instruction.getElseif().forEach(elif -> new InsIf(instruction.getPosition(), elif.getFst(), elif.getSnd(), new ArrayList<>()).accept(this));

        // If a return type was given for the else block, error
        if (instruction.getBodyElse().isPresent()) {
            Optional<Type> elseReturnType = instruction.getBodyElse().get().accept(this);
            elseReturnType.ifPresent(type -> this.errors.add("At " + instruction.getPosition() + ": No return expected, but given " + type));
        }

        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InsAssign instruction) {
        // Get the type of the lvalue
        Optional<Type> lvalue = instruction.getlValue().accept(this);
        assert lvalue.isPresent();
        // Get the type of the rvalue
        Optional<Type> rvalue = instruction.getExpression().accept(this);
        assert rvalue.isPresent();

        // If the operation is an assign, check if types are equals
        if (instruction.getOperation().equals(EnumAssignOp.EQUAL)) {
            if (!lvalue.get().equals(rvalue.get()))
                this.errors.add("At " + instruction.getPosition() + ": Trying to assign type "
                        + rvalue.get() + " to a variable of type "
                        + lvalue.get());
        } else {
            // If operation is an arithmetical assign, check if both types are Integers
            if (!lvalue.get().equals(Signature.Integer) || !rvalue.get().equals(Signature.Integer)) {
                this.errors.add("At " + instruction.getPosition() + ": Can't use operator "
                        + instruction.getOperation() + " on types "
                        + rvalue.get() + " and "
                        + lvalue.get()
                        + "(type " + Signature.Integer + " expected)");
            }
        }

        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InsBlock instruction) {
        // Add the block to the stack
        this.visitedBlocks.enter(instruction);

        //FIXME Clean this mess
        Optional<Type> returnTypeDetected = Optional.empty();
        for (Instruction i : instruction.getBody()) {
            if (i instanceof InsReturn) {
                if (returnTypeDetected.isEmpty()) returnTypeDetected = i.accept(this);
                else {
                    Optional<Type> currentReturnType = i.accept(this);
                    assert currentReturnType.isPresent();

                    if (!returnTypeDetected.equals(currentReturnType)) {
                        this.errors.add("At " + instruction.getPosition() + ": Multiple return instructions with different types found, previous at "
                                + returnTypeDetected.get().getPosition() + " had type " + returnTypeDetected.get() + " while return at "
                                + currentReturnType.get().getPosition() + " has type " + currentReturnType.get());
                    }
                }
            } else i.accept(this);
        }
        // Remove the block from the stack
        this.visitedBlocks.exit();

        // Return the first detected return type, by default, it's Optional.empty() if no InsReturn were found
        return returnTypeDetected;
    }

    @Override
    public Optional<Type> visit(InsBreak instruction) {
        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InsExpression instruction) {
        // Return the type of the expression
        return instruction.getExpression().accept(this);
    }

    @Override
    public Optional<Type> visit(InsReturn instruction) {
        // Return the type of the returned value
        return instruction.getExpression().accept(this);
    }
}

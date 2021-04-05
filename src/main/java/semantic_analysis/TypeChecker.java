package semantic_analysis;

import ast.*;
import support.Errors;
import support.Pair;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        Signature testedSignature = Signatures.unary.get(operation.getOperator());
        Optional<Type> expressionType = operation.getExpression().accept(this);

        assert expressionType.isPresent();

        if (!testedSignature.check(expressionType.get())) {
            this.errors.add("At " + operation.getPosition() + ": argument has type "
                    + expressionType.get() + " but type " + testedSignature.getArgumentsTypes().get(0)
                    + " is expected.");
        }
        return testedSignature.getReturnType();
    }

    @Override
    public Optional<Type> visit(ExpBinaryOperation operation) {
        Optional<Type> left = operation.getLeft().accept(this);
        Optional<Type> right = operation.getRight().accept(this);

        assert left.isPresent();
        assert right.isPresent();

        if (operation.getOperator().equals(EnumBinaryOp.EQ) || operation.getOperator().equals(EnumBinaryOp.DIFF)) {
            if (left != right)
                errors.add("Expression at " + operation.getLeft().getPosition()
                        + " should have the same type "
                        + "than expression at "
                        + operation.getRight().getPosition() + ".");
            return Optional.of(Signature.Bool);
        } else {
            Signature testedSignature = Signatures.binary.get(operation.getOperator());
            if (!testedSignature.check(left.get(), right.get())) {
                this.errors.add("At " + operation.getPosition() + ": arguments have types "
                        + left + " and " + right + " but types "
                        + testedSignature.getArgumentsTypes().get(0) + " and "
                        + testedSignature.getArgumentsTypes().get(1)
                        + " are expected.");
            }
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
        return this.symbolTable.variableLookup(variable.getVariable(), this.visitedBlocks);
    }

    @Override
    public Optional<Type> visit(ExpRecordAccess record) {
        // FIXME
        // Can't find how to recover "in a clean way" the name of the record
        TypVariable recordType = (TypVariable) record.getRecord().accept(this).get();
        Optional<List<Pair<String, Type>>> recordFields = this.symbolTable.typeDefinitionLookup(recordType.getName());
        assert recordFields.isPresent();

        return Optional.of(recordFields.get().stream().filter(pair -> pair.getFst().equals(record.getField())).findFirst().get().getSnd());
    }

    @Override
    public Optional<Type> visit(ExpArrayAccess array) {
        Optional<Type> indexType = array.getIndex().accept(this);
        assert indexType.isPresent();
        if (!indexType.get().equals(Signature.Integer)) {
            this.errors.add("At " + array.getPosition() + ": index has type "
                    + indexType.get() + " but type "
                    + Signature.Integer
                    + " is expected.");
        }
        return array.getArray().accept(this);
    }

    @Override
    public Optional<Type> visit(ExpArrayEnum enumeration) {
        if (enumeration.getElements().size() == 0) return this.defaultValue;
        Optional<Type> firstType = enumeration.getElements().get(0).accept(this);
        assert firstType.isPresent();
        for (Expression exp : enumeration.getElements()) {
            Optional<Type> currentType = exp.accept(this);
            assert currentType.isPresent();
            if (!firstType.get().equals(currentType.get())) {
                this.errors.add("At " + enumeration.getPosition() + ": arguments have different types ("
                        + firstType.get() + " and " + currentType.get() + ")");
            }
        }
        return firstType;
    }

    @Override
    public Optional<Type> visit(ExpRecordEnum enumeration) {
        Optional<Pair<String, List<Pair<String, Type>>>> optionalType = this.symbolTable.typeNameLookup(enumeration.getFieldValues().stream().map(Pair::getFst).collect(Collectors.toList()));

        if (optionalType.isEmpty()) {
            this.errors.add("At " + enumeration.getPosition() + ": No record were found matching the following fields ("
                    + enumeration.getFieldValues().stream().map(Pair::getFst).collect(Collectors.joining(", ")) + ")");
        }

        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(ExpFunctionCall function) {
        List<Signature> signatures = this.symbolTable.functionLookup(function.getName());
        assert !signatures.isEmpty();
        List<Type> expCallArgumentsType = function.getArguments().stream().map(arg -> arg.accept(this).get()).collect(Collectors.toList());
        for (Signature currentSignature : signatures) {
            if (currentSignature.check(expCallArgumentsType)) {
                return currentSignature.getReturnType();
            }
        }
        this.errors.add("At " + function.getPosition() + ": No matching function found with arguments type ("
                + expCallArgumentsType.stream().map(Type::toString).collect(Collectors.joining(", ")) + ")");
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(ExpString string) {
        return Optional.of(Signature.String);
    }

    @Override
    public Optional<Type> visit(ExpNew expression) {
        Optional<Type> argumentType = expression.getArgument().accept(this);
        assert argumentType.isPresent();

        if (!expression.getType().equals(argumentType.get())) {
            this.errors.add("At " + expression.getPosition() + ": Argument has type "
                    + argumentType.get() + ", but type "
                    + expression.getType() + " was expected");
        }
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
        return Optional.of(typDictionary.getType());
    }

    @Override
    public Optional<Type> visit(TypVariable typRecord) {
        return Optional.of(typRecord);
    }

    @Override
    public Optional<Type> visit(TypArray typArray) {
        return Optional.of(typArray.getType());
    }

    @Override
    public Optional<Type> visit(Function function) {
        function.getParameters().stream().map(decl -> decl.accept(this));
        function.getBody().accept(this);
        // Result is never used
        return function.getReturn_type();
    }

    @Override
    public Optional<Type> visit(TypeDefinition typeDefinition) {
        typeDefinition.getDeclarations().stream().map(decl -> decl.accept(this));
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
        if (globalDeclaration.getExpression().isPresent()) {
            return globalDeclaration.getExpression().get().accept(this);
        } else return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(Program program) {
        program.getImports().stream().map(imp -> imp.accept(this));
        program.getGlobalDeclarations().stream().map(glob -> glob.accept(this));
        program.getTypeDefinitions().stream().map(typ -> typ.accept(this));
        program.getFunctions().stream().map(func -> func.accept(this));
        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(Declaration declaration) {
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

        Optional<Type> declarationType = instruction.getDeclaration().accept(this);
        assert declarationType.isPresent();
        if (!declarationType.get().equals(Signature.Integer)) {
            this.errors.add("At " + instruction.getPosition() + ": iterator declaration is of type "
                    + declarationType.get() + ", but type "
                    + Signature.Integer + " expected");
        }

        Optional<Type> rangeType = instruction.getRange().accept(this);
        assert rangeType.isPresent();
        if (!rangeType.get().equals(Signature.Bool)) {
            this.errors.add("At " + instruction.getPosition() + ": range condition is of type "
                    + rangeType.get() + ", but type "
                    + Signature.Bool + " expected");
        }

        Optional<Type> stepType = instruction.getStep().accept(this);
        assert stepType.isPresent();
        if (!stepType.get().equals(Signature.Integer)) {
            this.errors.add("At " + instruction.getPosition() + ": step is of type "
                    + stepType.get() + ", but type "
                    + Signature.Integer + " expected");
        }

        instruction.getBody().accept(this);

        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InstForeach instruction) {
        Optional<Type> declarationType = instruction.getDeclaration().accept(this);
        assert declarationType.isPresent();
        Optional<Type> collectionType = instruction.getCollection().accept(this);
        assert collectionType.isPresent();

        if (!declarationType.get().equals(collectionType.get())) {
            this.errors.add("At " + instruction.getPosition() + ": declaration of type "
                    + declarationType.get() + ", but collection of type "
                    + collectionType.get());
        }

        instruction.getBody().accept(this);

        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InsWhile instruction) {
        Optional<Type> conditionType = instruction.getCondition().accept(this);
        assert conditionType.isPresent();
        if (!conditionType.get().equals(Signature.Bool)) {
            this.errors.add("At " + instruction.getPosition() + ": condition is of type "
                    + conditionType.get() + ", but type "
                    + Signature.Bool + " expected");
        }

        instruction.getBody().accept(this);

        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InsIf instruction) {
        Optional<Type> conditionType = instruction.getCondition().accept(this);
        assert conditionType.isPresent();
        if (!conditionType.get().equals(Signature.Bool))
            this.errors.add("At " + instruction.getPosition() + ": condition is of type "
                    + conditionType.get() + ", but type "
                    + Signature.Bool + " expected");

        instruction.getBody().accept(this);
        instruction.getElseif().stream().map(elif -> new InsIf(instruction.getPosition(), elif.getFst(), elif.getSnd(), new ArrayList<>()).accept(this));
        if (instruction.getBodyElse().isPresent()) instruction.getBodyElse().get().accept(this);

        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InsAssign instruction) {
        Optional<Type> lvalue = instruction.getlValue().accept(this);
        assert lvalue.isPresent();
        Optional<Type> rvalue = instruction.getExpression().accept(this);
        assert rvalue.isPresent();

        if (instruction.getOperation().equals(EnumAssignOp.EQUAL)) {
            if (!lvalue.get().equals(rvalue.get()))
                this.errors.add("At " + instruction.getPosition() + ": Trying to assign type "
                        + rvalue.get() + " to a variable of type "
                        + lvalue.get());
        } else {
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
        this.visitedBlocks.enter(instruction);
        instruction.getBody().stream().map(inst -> inst.accept(this));
        this.visitedBlocks.exit();
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InsBreak instruction) {
        // Result is never used
        return this.defaultValue;
    }

    @Override
    public Optional<Type> visit(InsExpression instruction) {
        return instruction.getExpression().accept(this);
    }

    @Override
    public Optional<Type> visit(InsReturn instruction) {
        return instruction.getExpression().accept(this);
    }
}

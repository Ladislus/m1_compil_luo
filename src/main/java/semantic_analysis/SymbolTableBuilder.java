package semantic_analysis;

import ast.*;
import support.Errors;
import support.ListTools;
import support.Pair;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static semantic_analysis.Signature.signatureOf;

public class SymbolTableBuilder
        extends ast.VisitorBase<Void> {

    private final SymbolTable symbolTable;
    private final Errors errors;
    private final VisitedBlocks visitedBlocks;

    public SymbolTableBuilder() {
        this.errors = new Errors();
        this.symbolTable = new SymbolTable();
        Signatures.addPredefinedSignature(this.symbolTable);
        this.visitedBlocks = new VisitedBlocks();
    }

    public Errors getErrors() {
        return errors;
    }

    public SymbolTable getSymbolTable(){
        if (errors.hasErrors())
            throw new Error("Semantic analysis detected errors");
        return symbolTable;
    }

    private static List<Pair<String, Type>> fieldsDefinitionOf(TypeDefinition typeDefinition) {
        return typeDefinition.getDeclarations().stream()
                .map((decl)-> new Pair<>(decl.getVariable(), decl.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public Void visit(Function function) {
        String functionName = function.getName();
        Signature signature = signatureOf(function);
        if (existsSignatureWithSameArgumentTypes(functionName, signature))
            errors.add(existsSignatureWithSameArgumentTypesMessage(function.getPosition(), functionName));
        else
            symbolTable.addFunction(functionName, signature);
        addFunctionArgumentsAsLocalVariables(function);
        function.getBody().accept(this);
        return null;
    }

    @Override
    public Void visit(TypeDefinition typeDefinition) {
        String typeName = typeDefinition.getName();
        Position position = typeDefinition.getPosition();
        if (typeDefinitionNameAlreadyExists(typeDefinition))
            errors.add(typeDefinitionNameAlreadyExistsMessage(position, typeName));
        else{
            if (typeDefinitionFieldsAlreadyUsed(typeDefinition))
                errors.add(typeDefinitionFieldsAlreadyUsedMessage(position, typeName));
            else
            if (typeDefinitionHasDuplicateFields(typeDefinition))
                errors.add(typeDefinitionHasDuplicateFieldsMessage(position, typeName));
            symbolTable.addTypeDefinition(typeName, fieldsDefinitionOf(typeDefinition));
        }
        return null;
    }

    private boolean existsSignatureWithSameArgumentTypes(String functionName,
                                                         Signature signature) {
        List<Signature> existingSignatures = symbolTable.functionLookup(functionName);
        return
                existingSignatures.stream()
                        .anyMatch((sig) -> signature.getArgumentsTypes().equals(sig.getArgumentsTypes()));
    }

    private String existsSignatureWithSameArgumentTypesMessage(Position position, String functionName){
        return "At position " + position
                + " the signature of the function "
                + functionName + " makes the overloading ambiguous.";
    }

    private void addFunctionArgumentsAsLocalVariables(Function function) {
        InsBlock functionBody = function.getBody();
        symbolTable.createLocalTable(functionBody);
        visitedBlocks.enter(functionBody);
        function.getParameters().forEach((declaration -> declaration.accept(this)));
        visitedBlocks.exit();
    }

    public boolean typeDefinitionNameAlreadyExists(TypeDefinition typeDefinition) {
        String typeName = typeDefinition.getName();
        Optional<List<Pair<String, Type>>> existingDefinition =
                symbolTable.typeDefinitionLookup(typeName);
        return existingDefinition.isPresent();
    }
    private String typeDefinitionNameAlreadyExistsMessage(Position position, String typeName) {
        return "At position " + position
                + " type " + typeName + " is already defined elsewhere.";
    }

    public boolean typeDefinitionFieldsAlreadyUsed(TypeDefinition typeDefinition) {
        List<Pair<String, Type>> fieldsDefinition = fieldsDefinitionOf(typeDefinition);
        List<String> fieldNames = ListTools.getFstList(fieldsDefinition);
        Optional<Pair<String, List<Pair<String, Type>>>> existingDefinitionWithSameFields =
                symbolTable.typeNameLookup(fieldNames);
        return existingDefinitionWithSameFields.isPresent();
    }

    private String typeDefinitionFieldsAlreadyUsedMessage(Position position, String typeName) {
        return "At position " + position
                + " type " + typeName + " uses the exact same list of"
                + "  field names than an already defined type.";
    }

    public boolean typeDefinitionHasDuplicateFields(TypeDefinition typeDefinition){
        List<String> fieldNames = ListTools.getFstList(fieldsDefinitionOf(typeDefinition));
        int size = fieldNames.size();
        for(int index = 0; index < size; index++)
            if (fieldNames.subList(index+1, size).contains(fieldNames.get(index)))
                return true;
        return false;
    }

    private String typeDefinitionHasDuplicateFieldsMessage(Position position, String typeName) {
        return "At position " + position + " type definition " + typeName
                + " uses two times the same field name.";
    }

    @Override
    public Void visit(Import imports) {
        errors.add("Imports are not supported.");
        return null;
    }

    @Override
    public Void visit(Declaration declaration) {
        String variableName = declaration.getVariable();
        Type variableType = declaration.getType();
        Optional<Type> existingType =
                symbolTable.variableLookup(variableName, visitedBlocks);
        if (ReservedWord.is(variableName))
            errors.add("At position " + declaration.getExpression()
                    + " variable " + variableName + " has an invalid name. "
                    + " It is a LUO reserved word.");
        if (existingType.isPresent())
            errors.add("At position " + declaration.getPosition()
                    + " variable " + variableName + " is already declared.");
        else {
            if (visitedBlocks.getStack().isEmpty())
                symbolTable.addGlobalVariable(variableName, variableType);
            else
                symbolTable.addVariable(visitedBlocks.current(), variableName, variableType);
        }
        return null;
    }

    @Override
    public Void visit(GlobalDeclaration globalDeclaration) {
        Declaration declaration = new Declaration(globalDeclaration);
        declaration.accept(this);
        return null;
    }

    @Override
    public Void visit(InsBlock block) {
        symbolTable.createLocalTable(block);
        visitedBlocks.enter(block);
        super.visit(block);
        visitedBlocks.exit();
        return null;
    }
}

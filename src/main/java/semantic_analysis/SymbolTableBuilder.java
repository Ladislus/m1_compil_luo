package semantic_analysis;

import ast.*;
import semantic_analysis.exceptions.*;

import java.util.*;

public class SymbolTableBuilder extends ast.VisitorBase<Void> {

    private final SymbolTable table = new SymbolTable();
    private final VisitedBlocks blocks = new VisitedBlocks();
    private final Map<Position, String> errors = new HashMap<>();
    private InsBlock current;

    @Override
    public Void visit(InsBlock instruction) {
        try {
            this.current = instruction;
            this.table.insertBlock(instruction);
            this.blocks.enter(instruction);
        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        }
        return super.visit(instruction);
    }

    @Override
    public Void visit(Function function) {
        List<Type> parameters = new ArrayList<>();
        for (Declaration d : function.getParameters())
            parameters.add(d.getType());
        Type returnType = function.getReturn_type().isPresent() ? function.getReturn_type().get() : Signature.Void;
        try {
            this.table.insertFunction(function.getName(), new Signature(parameters, returnType));
        } catch (FunctionSignatureAlreadyExistsException e) {
            e.printStackTrace();
        }
        return super.visit(function);
    }

    @Override
    public Void visit(GlobalDeclaration globalDeclaration) {
        try {
            this.table.insertGlobalVariable(globalDeclaration.getVariable(), globalDeclaration.getType());
        } catch (GlobalVariableAlreadyExistsException e) {
            e.printStackTrace();
        }
        return super.visit(globalDeclaration);
    }

    @Override
    public Void visit(Declaration declaration) {
        // TODO : Ensure current is never null
        if (Objects.isNull(current))
            throw new RuntimeException();
        try {
            this.table.insertVariable(declaration.getVariable(), current, declaration.getType());
        } catch (VariableAlreadyExistsInScopeException | BlockDosentExistsException e) {
            e.printStackTrace();
        }
        return super.visit(declaration);
    }

    @Override
    public Void visit(ExpVariable variable) {
        // TODO : Copy VisitedBlock, as varLookup empties the variable
        // TODO : better error management
        if (this.table.varLookup(variable.getVariable(), this.blocks).isEmpty())
            this.errors.put(variable.getPosition(), "Variable dosen't exists");
        return super.visit(variable);
    }

    // TODO implement function
    @Override
    public Void visit(ExpFunctionCall function) {
        if (this.table.funcLookup(function.getName()).isEmpty())
            this.errors.put(function.getPosition(), "There is no function named " + function.getName());
        else {
            List<Type> argumentsType = new ArrayList<>();
            for (Expression e : function.getArguments()) {
                // TODO get the type of the expression
                // argumentsType.add(e.???);
            }
            // TODO check if function signature exists
            // Signature sig = new Signature(argumentsType, ???);
            // if (!this.table.funcLookup(function.getName()).contains(sig))
            //    this.errors.put(function.getPosition(), "There is no method with corresponding signature : " + sig);
        }
        return super.visit(function);
    }

    // TODO Expression type checking (can't recover expression type)
}

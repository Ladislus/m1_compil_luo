package semantic_analysis;

import ast.Declaration;
import ast.ExpVariable;
import ast.Function;
import ast.GlobalDeclaration;
import ast.InsBlock;
import ast.Position;
import ast.Type;
import semantic_analysis.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            this.errors.put(instruction.getPosition(), e.format(instruction.getPosition()));
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
            this.errors.put(function.getPosition(), e.format(function.getPosition(), function.getName()));
        }
        return super.visit(function);
    }

    @Override
    public Void visit(GlobalDeclaration globalDeclaration) {
        try {
            this.table.insertGlobalVariable(globalDeclaration.getVariable(), globalDeclaration.getType());
        } catch (GlobalVariableAlreadyExistsException e) {
            this.errors.put(globalDeclaration.getPosition(), e.format(globalDeclaration.getPosition(), globalDeclaration.getVariable()));
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
        } catch (VariableAlreadyExistsInScopeException e) {
            this.errors.put(declaration.getPosition(), e.format(declaration.getPosition(), declaration.getVariable()));
        } catch (BlockDosentExistsException e) {
            this.errors.put(declaration.getPosition(), e.format(declaration.getPosition()));
        }
        return super.visit(declaration);
    }

    @Override
    public Void visit(ExpVariable variable) {
        try {
            this.table.varLookup(variable.getVariable(), this.blocks.copy());
        } catch (VariableDosentExistsException e) {
            this.errors.put(variable.getPosition(), e.format(variable.getPosition(), variable.getVariable()));
        }
        return super.visit(variable);
    }
}

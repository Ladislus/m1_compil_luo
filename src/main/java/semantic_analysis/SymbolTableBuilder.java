package semantic_analysis;

import ast.Declaration;
import ast.ExpFunctionCall;
import ast.ExpVariable;
import ast.Function;
import ast.GlobalDeclaration;
import ast.InsBlock;
import ast.Position;
import ast.Program;
import ast.Type;
import ast.TypeDefinition;
import semantic_analysis.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SymbolTableBuilder extends ast.VisitorBase<Void> {

    private final SymbolTable table = new SymbolTable();
    private final VisitedBlocks blocks = new VisitedBlocks();
    private final List<String> errors = new ArrayList<>();

    private InsBlock current;
    private final Map<String, Position> functionCalls = new HashMap<>();

    public void print() {
        this.table.print();
        if (!this.errors.isEmpty()) {
            System.out.println("Errors: ");
            for (String e : this.errors)
                System.out.println(e);
        }
    }

    @Override
    public Void visit(Program program) {
        super.visit(program);
        for (String call : this.functionCalls.keySet()) {
            if (this.table.funcLookup(call).isEmpty()) {
                this.errors.add(this.functionCalls.get(call) + " Function " + call + " called, but not defined");
            }
        }
        return null;
    }

    @Override
    public Void visit(InsBlock instruction) {
        try {
            this.current = instruction;
            this.table.insertBlock(instruction);
            this.blocks.enter(instruction);
        } catch (BlockAlreadyExistsException e) {
//            this.errors.add(e.format(instruction.getPosition()));
        }
        return super.visit(instruction);
    }

    @Override
    public Void visit(Function function) {
        this.current = function.getBody();
        try {
            this.table.insertBlock(function.getBody());
        } catch (BlockAlreadyExistsException e) {
//            this.errors.add(e.format(function.getPosition()));
        }
        List<Type> parameters = new ArrayList<>();
        for (Declaration d : function.getParameters())
            parameters.add(d.getType());
        Type returnType = function.getReturn_type().isPresent() ? function.getReturn_type().get() : Signature.Void;
        try {
            this.table.insertFunction(function.getName(), new Signature(parameters, returnType));
        } catch (FunctionSignatureAlreadyExistsException e) {
            this.errors.add(e.format(function.getPosition(), function.getName()));
        }
        return super.visit(function);
    }

    @Override
    public Void visit(GlobalDeclaration globalDeclaration) {
        try {
            this.table.insertGlobalVariable(globalDeclaration.getVariable(), globalDeclaration.getType());
        } catch (GlobalVariableAlreadyExistsException e) {
            this.errors.add(e.format(globalDeclaration.getPosition(), globalDeclaration.getVariable()));
        }
        return super.visit(globalDeclaration);
    }

    @Override
    public Void visit(Declaration declaration) {
        if (Objects.isNull(current))
            try {
                this.table.insertGlobalVariable(declaration.getVariable(), declaration.getType());
            } catch (GlobalVariableAlreadyExistsException e) {
                this.errors.add(e.format(declaration.getPosition(), declaration.getVariable()));
            }
        else {
            try {
                this.table.insertVariable(declaration.getVariable(), current, declaration.getType());
            } catch (VariableAlreadyExistsInScopeException e) {
                this.errors.add(e.format(declaration.getPosition(), declaration.getVariable()));
            } catch (BlockDosentExistsException e) {
                this.errors.add(e.format(declaration.getPosition()));
            }
        }
        return super.visit(declaration);
    }

    @Override
    public Void visit(ExpVariable variable) {
        try {
            this.table.varLookup(variable.getVariable(), this.blocks.copy());
        } catch (VariableDosentExistsException e) {
            this.errors.add(e.format(variable.getPosition(), variable.getVariable()));
        }
        return super.visit(variable);
    }

    @Override
    public Void visit(ExpFunctionCall function) {
        this.functionCalls.put(function.getName(), function.getPosition());
        return super.visit(function);
    }

    @Override
    public Void visit(TypeDefinition typeDefinition) {
        try {
            this.table.insertUserType(typeDefinition);
        } catch (UserTypeAlreadyExistsException e) {
            this.errors.add(e.format(typeDefinition.getPosition(), typeDefinition.getName()));
        }
        return super.visit(typeDefinition);
    }
}

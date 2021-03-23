package semantic_analysis;

import ast.*;
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

    private String definitionName = null;
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

    private void insertUserType(TypeDefinition typeDefinition) {
        try {
            this.table.insertUserType(typeDefinition.getName());
        } catch (UserTypeAlreadyExistsException e) {
            this.errors.add(e.format(typeDefinition.getPosition(), typeDefinition.getName()));
        }
    }

    private void insertUserTypeVariable(Declaration declaration) {
        try {
            this.table.insertUserTypeVariable(this.definitionName, declaration.getVariable(), declaration.getType());
        } catch (UserTypeNotDefinedException e) {
            this.errors.add(e.format(declaration.getPosition(), definitionName));
        } catch (UserTypeFieldAlreadyExistsException e) {
            this.errors.add(e.format(declaration.getPosition(), declaration.getVariable()));
        }
    }

    private void insertFunction(Function function) {
        List<Type> parameters = new ArrayList<>();
        for (Declaration d : function.getParameters())
            parameters.add(d.getType());
        Type returnType = function.getReturn_type().isPresent() ? function.getReturn_type().get() : Signature.Void;
        try {
            this.table.insertFunction(function.getName(), new Signature(parameters, returnType));
        } catch (FunctionSignatureAlreadyExistsException e) {
            this.errors.add(e.format(function.getPosition(), function.getName()));
        }
    }

    private void insertGlobalVariable(Declaration globalDeclaration) {
        try {
            this.table.insertGlobalVariable(globalDeclaration.getVariable(), globalDeclaration.getType());
        } catch (GlobalVariableAlreadyExistsException e) {
            this.errors.add(e.format(globalDeclaration.getPosition(), globalDeclaration.getVariable()));
        }
    }

    private void insertVariable(Declaration declaration) {
        try {
            this.table.insertVariable(declaration.getVariable(), current, declaration.getType(), this.blocks.copy());
        } catch (VariableAlreadyExistsInScopeException e) {
            this.errors.add(e.format(declaration.getPosition(), declaration.getVariable()));
        } catch (BlockDosentExistsException e) {
            this.errors.add(e.format(declaration.getPosition()));
        }
    }

    private void varLookup(ExpVariable variable) {
        try {
            this.table.varLookup(variable.getVariable(), this.blocks.copy());
        } catch (VariableDosentExistsException e) {
            this.errors.add(e.format(variable.getPosition(), variable.getVariable()));
        }
    }

    private void verifyCalls() {
        for (String call : this.functionCalls.keySet())
            if (!call.equals(EnumPredefinedOp.LENGTH.toString()) && !call.equals(EnumPredefinedOp.FREE.toString()))
                if (this.table.funcLookup(call).isEmpty())
                    this.errors.add(this.functionCalls.get(call) + " Function " + call + " called, but not defined");
    }

    @Override
    public Void visit(Program program) {
        super.visit(program);
        this.verifyCalls();
        return null;
    }

    @Override
    public Void visit(InsBlock instruction) {
        this.current = instruction;
        this.table.insertBlock(instruction);
        this.blocks.enter(instruction);
        Void v = super.visit(instruction);
        this.blocks.exit();
        return v;
    }

    @Override
    public Void visit(Function function) {
        this.current = function.getBody();
        this.table.insertBlock(function.getBody());
        this.blocks.enter(function.getBody());
        this.insertFunction(function);
        Void v = super.visit(function);
        this.blocks.exit();
        return v;
    }

    @Override
    public Void visit(GlobalDeclaration globalDeclaration) {
        this.insertGlobalVariable(globalDeclaration);
        return super.visit(globalDeclaration);
    }

    @Override
    public Void visit(Declaration declaration) {
        if (Objects.isNull(current) && !Objects.isNull(this.definitionName))
            this.insertUserTypeVariable(declaration);
        else
            this.insertVariable(declaration);
        return super.visit(declaration);
    }

    @Override
    public Void visit(ExpVariable variable) {
        this.varLookup(variable);
        return super.visit(variable);
    }

    @Override
    public Void visit(ExpFunctionCall function) {
        this.functionCalls.put(function.getName(), function.getPosition());
        return super.visit(function);
    }

    @Override
    public Void visit(TypeDefinition typeDefinition) {
        this.definitionName = typeDefinition.getName();
        this.insertUserType(typeDefinition);
        Void v = super.visit(typeDefinition);
        this.definitionName = null;
        return v;
    }
}

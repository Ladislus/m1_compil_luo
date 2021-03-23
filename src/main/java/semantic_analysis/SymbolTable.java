package semantic_analysis;

import ast.*;
import semantic_analysis.exceptions.*;
import support.Pair;

import java.util.*;

public class SymbolTable {

    private final Map<String, List<Signature>> functions = new HashMap<>();
    private final Map<String, Type> globalVariables = new HashMap<>();
    private final Map<String, List<Pair<String, Type>>> userTypes = new HashMap<>();
    private final Map<InsBlock, Map<String, Type>> variables = new HashMap<>();

    public void print() {
        System.out.println("Functions: ");
        for (String f : this.functions.keySet())
            System.out.println("\t" + f + ": " + this.functions.get(f));

        System.out.println("Global variables: ");
        for (String v : this.globalVariables.keySet())
            System.out.println("\t" + v + ": " + this.globalVariables.get(v));

        System.out.println("User types: ");
        for (String t : this.userTypes.keySet())
            System.out.println("\t" + t + ": " + this.userTypes.get(t));

        System.out.println("Variables: ");
        for (InsBlock vars : this.variables.keySet()) {
            System.out.println("\t" + vars);
            for (String v : this.variables.get(vars).keySet())
                System.out.println("\t\t" + v + ": " + this.variables.get(vars).get(v));
        }
    }

    public void insertUserType(TypeDefinition typedef) throws UserTypeAlreadyExistsException {
        if (!this.userTypes.containsKey(typedef.getName())) {
            List<Pair<String, Type>> types = new ArrayList<>();
            for (Declaration d : typedef.getDeclarations())
                types.add(new Pair<>(d.getVariable(), d.getType()));
            this.userTypes.put(typedef.getName(), types);
        }
        else throw new UserTypeAlreadyExistsException();
    }

    public void insertBlock(InsBlock block) {
        if (!this.variables.containsKey(block))
            this.variables.put(block, new HashMap<>());
    }

    public void insertFunction(String functionName, Signature functionSignature) throws FunctionSignatureAlreadyExistsException {
        for (EnumPredefinedOp op : Signatures.premade.keySet())
            if (op.toString().equals(functionName) && Signatures.premade.get(op).equals(functionSignature))
                throw new FunctionSignatureAlreadyExistsException();
        if (this.functions.containsKey(functionName)) {
            if (!this.functions.get(functionName).contains(functionSignature))
                this.functions.get(functionName).add(functionSignature);
            else
                throw new FunctionSignatureAlreadyExistsException();
        }
        else {
            List<Signature> elem = new ArrayList<>();
            elem.add(functionSignature);
            this.functions.put(functionName, elem);
        }
    }

    public void insertGlobalVariable(String variableName, Type variableType) throws GlobalVariableAlreadyExistsException {
        if (this.globalVariables.containsKey(variableName))
            throw new GlobalVariableAlreadyExistsException();
        this.globalVariables.put(variableName, variableType);
    }

    public void insertVariable(String variableName, InsBlock block, Type variableType) throws VariableAlreadyExistsInScopeException, BlockDosentExistsException {
        if (!this.variables.containsKey(block))
            throw new BlockDosentExistsException();
        if (this.variables.get(block).containsKey(variableName))
            throw new VariableAlreadyExistsInScopeException();
        this.variables.get(block).put(variableName, variableType);
    }

    List<Signature> funcLookup(String functionName) {
        List<Signature> signs = new ArrayList<>();
        for (EnumPredefinedOp op : Signatures.premade.keySet())
            if (functionName.equals(op.toString())) signs.add(Signatures.premade.get(op));
        if (this.functions.containsKey(functionName))
            signs.addAll(this.functions.get(functionName));
        return signs;
    }

    Optional<Type> varLookup(String variable, VisitedBlocks visitedBlocks) throws VariableDosentExistsException {
        while(!visitedBlocks.getStack().empty()) {
            if (this.variables.get(visitedBlocks.current()).containsKey(variable))
                return Optional.of(this.variables.get(visitedBlocks.current()).get(variable));
            visitedBlocks.exit();
        }
        if (this.globalVariables.containsKey(variable))
            return Optional.of(this.globalVariables.get(variable));
        throw new VariableDosentExistsException();
    }
}

package semantic_analysis;

import ast.InsBlock;
import ast.Type;
import semantic_analysis.exceptions.*;
import support.Pair;

import java.util.*;

public class SymbolTable {

    private final Map<String, List<Signature>> functions = new HashMap<>();
    private final Map<String, Type> globalVariables = new HashMap<>();
    private final Map<String, List<Pair<String, Type>>> userTypes = new HashMap<>();
    private final Map<InsBlock, Map<String, Type>> variables = new HashMap<>();

    public void insertBlock(InsBlock block) throws BlockAlreadyExistsException {
        if (this.variables.containsKey(block))
            throw new BlockAlreadyExistsException();
        else this.variables.put(block, new HashMap<>());
    }

    public void insertFunction(String functionName, Signature functionSignature) throws FunctionSignatureAlreadyExistsException {
        if (this.functions.containsKey(functionName)) {
            if (!this.functions.get(functionName).contains(functionSignature))
                this.functions.get(functionName).add(functionSignature);
            else throw new FunctionSignatureAlreadyExistsException();
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
        this.globalVariables.put(variableName, variableType);
    }

    List<Signature> funcLookup(String functionName) {
        if (this.functions.containsKey(functionName))
            return this.functions.get(functionName);
        return new ArrayList<>();
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

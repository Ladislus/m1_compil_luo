package semantic_analysis;

import ast.Type;
import support.Pair;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class SymbolTable {

    private final List<Pair<String, Signature>> functions = new ArrayList<>();
    private final Map<String, Type> variables = new HashMap<>();

    public void insertFunction(String functionName, Signature functionSignature) { this.functions.add(new Pair<>(functionName, functionSignature)); }
    public void insertVariable(String variableName, Type variableType) { this.variables.put(variableName, variableType); }

    // TODO
    List<Signature> funcLookup(String functionName) { return null; }
    // TODO
    Optional<Type> varLookup(String variable, VisitedBlocks visitedBlocks) { return null; }
}

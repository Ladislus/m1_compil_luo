package semantic_analysis.exceptions;

import ast.Position;

public class VariableAlreadyExistsInScopeException extends Exception {
    public String format(Position pos, String variableName) {
        return pos + " Variable '" + variableName + "' already defined";
    }
}

package semantic_analysis.exceptions;

import ast.Position;

public class VariableDosentExistsException extends Exception {
    public String format(Position pos, String variableName) {
        return pos + " Variable '" + variableName + "' isn't defined";
    }
}

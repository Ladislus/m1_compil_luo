package semantic_analysis.exceptions;

import ast.Position;

public class GlobalVariableAlreadyExistsException extends Exception {
    public String format(Position pos, String variableName) {
        return pos + " Global variable '" + variableName + "' already defined";
    }
}

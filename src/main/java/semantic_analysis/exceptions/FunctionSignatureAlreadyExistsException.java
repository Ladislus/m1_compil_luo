package semantic_analysis.exceptions;

import ast.Position;

public class FunctionSignatureAlreadyExistsException extends Exception {
    public String format(Position pos, String functionName) {
        return pos + " Function '" + functionName + "' already defined";
    }
}

package semantic_analysis.exceptions;

import ast.Position;

public class UserTypeAlreadyExistsException extends Exception {

    public String format(Position pos, String typename) {
        return pos + " User type " + typename + " is already defined";
    }
}

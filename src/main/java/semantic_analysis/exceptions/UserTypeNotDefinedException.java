package semantic_analysis.exceptions;

import ast.Position;

public class UserTypeNotDefinedException extends Exception {

    public String format(Position pos, String typename) {
        return pos + " User type '" + typename + "' is not defined";
    }
}

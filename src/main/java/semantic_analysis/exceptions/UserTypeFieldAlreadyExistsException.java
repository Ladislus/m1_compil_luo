package semantic_analysis.exceptions;

import ast.Position;

public class UserTypeFieldAlreadyExistsException extends Exception {
    public String format(Position pos, String fieldname) {
        return pos + " User type field '" + fieldname + "' is already defined";
    }
}

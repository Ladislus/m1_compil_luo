package semantic_analysis.exceptions;

import ast.Position;

public class BlockDosentExistsException extends Exception {
    public String format(Position pos) {
        return pos + "(Compiler) The block dosen't exists in the table";
    }
}

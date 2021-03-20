package semantic_analysis.exceptions;

import ast.Position;

public class BlockAlreadyExistsException extends Exception {
    public String format(Position pos) {
        return pos + "(Compiler) The block already exists in the table";
    }
}

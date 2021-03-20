package ast;

import java.util.List;
import java.util.Objects;

public class InsBlock extends Instruction {

    private final List<Declaration> declarations;
    private final List<Instruction> body;

    public InsBlock(Position position, List<Declaration> declarations, List<Instruction> body) {
        this.position = position;
        this.declarations = declarations;
        this.body = body;
    }

    public List<Declaration> getDeclarations() {
        return this.declarations;
    }

    public List<Instruction> getBody() {
        return this.body;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsBlock insBlock = (InsBlock) o;
        return declarations.equals(insBlock.declarations) && body.equals(insBlock.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(declarations, body);
    }
}

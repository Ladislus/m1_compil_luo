package ast;

import java.util.List;

public class InsBlock extends Instruction {

    private final List<Declaration> declarations;
    private final List<Instruction> body;

    public InsBlock(List<Declaration> declarations, List<Instruction> body) {
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
}

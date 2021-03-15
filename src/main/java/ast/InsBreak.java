package ast;

public class InsBreak extends Instruction {

    public InsBreak(Position position) {
        this.position = position;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

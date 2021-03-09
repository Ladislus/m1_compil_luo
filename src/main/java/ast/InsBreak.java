package ast;

public class InsBreak extends Instruction {

    public InsBreak() {
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

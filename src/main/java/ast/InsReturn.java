package ast;

public class InsReturn extends Instruction {

    private final Expression expression;

    public InsReturn(Position position, Expression expression) {
        this.position = position;
        this.expression = expression;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

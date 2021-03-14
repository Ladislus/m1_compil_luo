package ast;

public class InsExpression extends Instruction {

    private final Expression expression;

    public InsExpression(Position position, Expression expression) {
        this.position = position;
        this.expression = expression;
    }

    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

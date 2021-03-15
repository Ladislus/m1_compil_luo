package ast;

public class ExpPostUnaryOperation extends Expression {

    private final Expression expression;
    private final EnumUnaryOp operator;

    public ExpPostUnaryOperation(Position position, Expression expression, EnumUnaryOp operator) {
        this.position = position;
        this.expression = expression;
        this.operator = operator;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public EnumUnaryOp getOperator() {
        return this.operator;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

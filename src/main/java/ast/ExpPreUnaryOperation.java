package ast;

public class ExpPreUnaryOperation extends Expression {

    private final Expression expression;
    private final EnumUnaryOp operator;

    public ExpPreUnaryOperation(Expression expression, EnumUnaryOp operator) {
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
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
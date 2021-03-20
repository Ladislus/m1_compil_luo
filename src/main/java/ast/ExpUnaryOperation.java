package ast;

import java.util.Objects;

public class ExpUnaryOperation extends Expression {

    private final Expression expression;
    private final EnumUnaryOp operator;
    private final boolean prefix;

    public ExpUnaryOperation(Position position, Expression expression, EnumUnaryOp operator) {
        this.position = position;
        this.expression = expression;
        this.operator = operator;
        this.prefix = false;
    }

    public ExpUnaryOperation(Position position, EnumUnaryOp operator, Expression expression) {
        this.position = position;
        this.expression = expression;
        this.operator = operator;
        this.prefix = true;
    }

    public ExpUnaryOperation(Position position, EnumUnaryOp operator, Expression expression, boolean prefix) {
        this.position = position;
        this.expression = expression;
        this.operator = operator;
        this.prefix = prefix;
    }

    public boolean isPrefix() {
        return prefix;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpUnaryOperation that = (ExpUnaryOperation) o;
        return prefix == that.prefix && expression.equals(that.expression) && operator == that.operator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, operator, prefix);
    }
}

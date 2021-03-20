package ast;

import java.util.Objects;

public class ExpBinaryOperation extends Expression {

    private final Expression left;
    private final EnumBinaryOp operator;
    private final Expression right;

    public ExpBinaryOperation(Position position, Expression left, EnumBinaryOp operator, Expression right) {
        this.position = position;
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() {
        return this.left;
    }

    public EnumBinaryOp getOperator() { return this.operator; }

    public Expression getRight() {
        return this.right;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpBinaryOperation that = (ExpBinaryOperation) o;
        return left.equals(that.left) && operator == that.operator && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, operator, right);
    }
}

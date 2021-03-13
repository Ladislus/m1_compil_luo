package ast;

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
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

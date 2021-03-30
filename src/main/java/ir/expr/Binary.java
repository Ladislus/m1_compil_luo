package ir.expr;

import ir.Type;

public class Binary extends Expression
{
    private Expression left;
    private Expression right;
    private ast.EnumBinaryOp op;

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public ast.EnumBinaryOp getOp() {
        return op;
    }

    public Binary(Expression left, Expression right, ast.EnumBinaryOp op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public Type getType() {
        switch (op){
            case ADD:case DIV:case MOD: case MUL:case SUB: return Type.INT;
            default: return Type.BYTE;
        }
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

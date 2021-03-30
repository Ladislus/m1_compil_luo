package ir.expr;


import ast.EnumUnaryOp;
import ir.Type;

public class Unary extends Expression
{

    private Expression exp;
    private ast.EnumUnaryOp op;

    @Override
    public Type getType() {
        switch(op){
            case MINUS: return Type.INT;
            //ToDo: to check for LUO
            default: return Type.BYTE;
        }
    }

    public Expression getExp() {
        return exp;
    }

    public ast.EnumUnaryOp getOp() {
        return op;
    }

    public Unary(Expression exp, ast.EnumUnaryOp op) {
        this.exp = exp;
        this.op = op;
    }

    @Override
    public String toString() {
        return op + "(" + exp + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

package ir.expr;

import ir.Type;

public class Int extends Expression
{
    private int value;

    @Override
    public Type getType() {
        return Type.INT;
    }

    public int getValue() {
        return value;
    }

    public Int(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

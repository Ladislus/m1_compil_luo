package ir.expr;

import ir.Type;

public class Byte extends Expression
{
    private byte value;

    @Override
    public ir.Type getType() {
        return ir.Type.BYTE;
    }

    public int getValue() {
        return value;
    }

    public Byte(byte value) {
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

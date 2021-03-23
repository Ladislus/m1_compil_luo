package ir.expr;

import ir.Register;
import ir.RegisterOffset;
import ir.Type;

public class ReadMem extends Expression implements RegisterOffset {
    private Register reg;
    private Expression offset;
    private Type type;

    public Expression getOffset() {
        return offset;
    }

    public Register getRegister() {
        return reg;
    }

    @Override
    public Type getType() {
        return type;
    }

    public ReadMem(Register reg, Expression offset, Type type) {
        this.reg = reg;
        this.offset = offset;
        this.type = type;
    }

    @Override
    public String toString() {
        return reg + "[" + offset + "]" + ":" + type;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

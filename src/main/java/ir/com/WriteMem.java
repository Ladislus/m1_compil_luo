package ir.com;

import ir.Register;
import ir.RegisterOffset;
import ir.expr.Expression;
import ir.Type;

public class WriteMem extends Command implements RegisterOffset
{
    private Register register;
    private Expression offset;
    private Expression exp;
    private Type type;

    public Register getRegister() {
        return register;
    }

    public Expression getOffset() {
        return offset;
    }

    public Expression getExp() {
        return exp;
    }

    public Type getType() {
        return type;
    }

    public WriteMem(Register register, Expression offset, Expression exp, Type type) {
        this.register = register;
        this.offset = offset;
        this.exp = exp;
        this.type = type;
    }

    @Override
    public String toString() {
        return register + "[" + offset + "]" + " : " + type + " := " + exp;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

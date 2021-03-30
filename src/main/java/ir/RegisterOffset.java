package ir;

import ir.expr.Expression;

public interface RegisterOffset
{
    public Register getRegister();
    public Expression getOffset();
    public Type getType();
}

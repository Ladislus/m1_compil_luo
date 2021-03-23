package ir.expr;

import ir.Type;

abstract public class Expression {
    public abstract <T> T accept(Visitor<T> visitor);
    public abstract Type getType();
}

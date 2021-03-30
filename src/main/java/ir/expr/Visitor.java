package ir.expr;

public interface Visitor<T> {
    public T visit(Byte exp);
    public T visit(Int exp);
    public T visit(ReadReg exp);
    public T visit(ReadMem exp);
    public T visit(Unary exp);
    public T visit(Binary exp);
    public T visit(Symbol exp);
}


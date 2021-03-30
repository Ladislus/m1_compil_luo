package ir.com;

public interface Visitor<T>
{
    public T visit(Label com);
    public T visit(WriteReg com);
    public T visit(WriteMem com);
    public T visit(CJump com);
    public T visit(Jump com);
    public T visit(ProcCall com);
    public T visit(FunCall com);
}

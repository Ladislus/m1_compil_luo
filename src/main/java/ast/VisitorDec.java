package ast;

public interface VisitorDec<T> {
    T visit(Declaration declaration);
}

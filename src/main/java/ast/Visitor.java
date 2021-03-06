package ast;

public interface Visitor<T> extends VisitorTyp<T>, VisitorExp<T> {
    // TO COMPLETE
    T visit(Program program);
}

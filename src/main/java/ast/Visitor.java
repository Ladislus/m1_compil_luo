package ast;

public interface Visitor<T> extends VisitorTyp<T>, VisitorExp<T>, VisitorGlo<T>, VisitorDec<T>, VisitorInst<T> {

    // TO COMPLETE
    T visit(Program program);
}

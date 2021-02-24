package ast;

public interface Visitor<T> extends VisitorTyp<T>{
    // TO COMPLETE
    T visit(Program program);
}

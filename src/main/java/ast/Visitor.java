package ast;

public interface Visitor<T>
  extends VisitorTyp<T>, VisitorExp<T>,
    VisitorGlo<T>, VisitorDec<T>, VisitorInst<T> {
   T visit(Program program);
}

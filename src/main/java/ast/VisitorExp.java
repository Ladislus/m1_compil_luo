package ast;

public interface VisitorExp<T> {
    T visit(Expression expression);
    T visit(ExpPreUnaryOperation operation);
    T visit(ExpPostUnaryOperation operation);
    T visit(ExpBinaryOperation operation);
    T visit(ExpBoolean operation);
    T visit(ExpCharacter operation);
    T visit(ExpInteger operation);
    T visit(ExpString operation);
}

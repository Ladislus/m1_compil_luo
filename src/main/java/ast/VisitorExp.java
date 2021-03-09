package ast;

public interface VisitorExp<T> {
    T visit(ExpPreUnaryOperation operation);
    T visit(ExpPostUnaryOperation operation);
    T visit(ExpBinaryOperation operation);
    T visit(ExpBoolean operation);
    T visit(ExpCharacter operation);
    T visit(ExpInteger operation);
    T visit(ExpVariable variable);
    T visit(ExpRecord record);
    T visit(ExpArray array);
    T visit(ExpEnum enumeration);
    T visit(ExpTuple tuple);
    T visit(ExpFunctionCall function);
    T visit(ExpString operation);
}

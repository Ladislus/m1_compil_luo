package ast;

public interface VisitorExp<T> {
    T visit(ExpPreUnaryOperation operation);
    T visit(ExpPostUnaryOperation operation);
    T visit(ExpBinaryOperation operation);
    T visit(ExpBoolean bool);
    T visit(ExpCharacter character);
    T visit(ExpInteger integer);
    T visit(ExpVariable variable);
    T visit(ExpRecordAccess record);
    T visit(ExpArrayAccess array);
    T visit(ExpEnum enumeration);
    T visit(ExpTuple tuple);
    T visit(ExpFunctionCall function);
    T visit(ExpString string);
}

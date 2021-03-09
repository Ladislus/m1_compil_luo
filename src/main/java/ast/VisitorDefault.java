package ast;

public class VisitorDefault<T> implements Visitor<T> {

    private T defaultValue;

    public VisitorDefault(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    // ##########################
    // #    Block Expression    #
    // ##########################
    /**
     * @author Ladislas WALCAK
     * @author Thomas QUETIER
     * @author Corentin HERVOCHON
     * @author Martin GUERRAUD
     */
    @Override
    public T visit(Expression expression) {
        return this.defaultValue;
    }

    @Override
    public T visit(ExpPreUnaryOperation operation) {
        return operation.getExpression().accept(this);
    }

    @Override
    public T visit(ExpPostUnaryOperation operation) {
        return operation.getExpression().accept(this);
    }

    @Override
    public T visit(ExpBinaryOperation operation) {
        operation.getLeft().accept(this);
        return operation.getRight().accept(this);
    }

    @Override
    public T visit(ExpBoolean operation) {
        return this.defaultValue;
    }

    @Override
    public T visit(ExpCharacter operation) {
        return this.defaultValue;
    }

    @Override
    public T visit(ExpInteger operation) {
        return this.defaultValue;
    }

    @Override
    public T visit(ExpVariable variable) {
        return this.defaultValue;
    }

    @Override
    public T visit(ExpRecord record) {
        return record.getRecord().accept(this);
    }

    @Override
    public T visit(ExpArray array) {
        array.getArray().accept(this);
        return array.getIndex().accept(this);
    }

    @Override
    public T visit(ExpEnum enumeration) {
        T curr = null;
        for (Expression exp : enumeration.getElements()) curr = exp.accept(this);
        return curr;
    }

    @Override
    public T visit(ExpTuple tuple) {
        tuple.getFirst().accept(this);
        return tuple.getSecond().accept(this);
    }

    @Override
    public T visit(ExpFunctionCall function) {
        T curr = null;
        for (Expression exp : function.getArgs()) curr = exp.accept(this);
        return curr;
    }

    @Override
    public T visit(ExpString operation) {
        return this.defaultValue;
    }
    // ##############################
    // #    Fin Block Expression    #
    // ##############################
}

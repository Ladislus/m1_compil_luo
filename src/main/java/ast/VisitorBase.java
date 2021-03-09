package ast;


public class VisitorBase<T> implements Visitor<T> {


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
        return null;
    }

    @Override
    public T visit(ExpCharacter operation) {
        return null;
    }

    @Override
    public T visit(ExpInteger operation) {
        return null;
    }

    @Override
    public T visit(ExpVariable variable) {
        return null;
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
        return null;
    }
    // ##############################
    // #    Fin Block Expression    #
    // ##############################
}

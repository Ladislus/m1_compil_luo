package ast;

public class ExpTuple extends Expression {

    private final Expression first;
    private final Expression second;

    public ExpTuple(Expression first, Expression second) {
        this.first = first;
        this.second = second;
    }

    public Expression getFirst() {
        return this.first;
    }

    public Expression getSecond() {
        return this.second;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

package ast;

public class ExpTuple extends Expression {

    private final Expression first;
    private final Expression second;

    public ExpTuple(Position position, Expression first, Expression second) {
        this.position = position;
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
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

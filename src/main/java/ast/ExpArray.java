package ast;

public class ExpArray extends Expression {

    private final Expression array;
    private final Expression index;

    public ExpArray(Expression array, Expression index) {
        this.array = array;
        this.index = index;
    }

    public Expression getIndex() {
        return this.index;
    }

    public Expression getArray() {
        return this.array;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

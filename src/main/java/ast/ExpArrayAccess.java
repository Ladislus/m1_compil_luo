package ast;

public class ExpArrayAccess extends Expression {

    private final Expression array;
    private final Expression index;

    public ExpArrayAccess(Position position, Expression array, Expression index) {
        this.position = position;
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

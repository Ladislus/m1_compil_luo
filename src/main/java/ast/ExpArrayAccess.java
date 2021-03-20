package ast;

import java.util.Objects;

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
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpArrayAccess that = (ExpArrayAccess) o;
        return array.equals(that.array) && index.equals(that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(array, index);
    }
}

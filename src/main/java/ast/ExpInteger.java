package ast;

import java.util.Objects;

public class ExpInteger extends Expression {

    private final int value;

    public ExpInteger(Position position, int value) {
        this.position = position;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpInteger that = (ExpInteger) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

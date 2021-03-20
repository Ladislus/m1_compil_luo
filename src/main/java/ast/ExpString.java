package ast;

import java.util.Objects;

public class ExpString extends Expression {

    private final String value;

    public ExpString(Position position, String value) {
        this.position = position;
        this.value = value;
    }

    public String getValue() { return this.value; }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpString expString = (ExpString) o;
        return value.equals(expString.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

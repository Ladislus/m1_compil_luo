package ast;

import java.util.Objects;

public class ExpCharacter extends Expression {

    private final char value;

    public ExpCharacter(Position position, char value) {
        this.position = position;
        this.value = value;
    }

    public char getValue() {
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
        ExpCharacter that = (ExpCharacter) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

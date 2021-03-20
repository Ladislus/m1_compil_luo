package ast;

import java.util.Objects;

public class TypArray extends Type {

    private final Type type;

    public TypArray(Position position, Type type) {
        this.position = position;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TypArray typArray = (TypArray) o;
        return type.equals(typArray.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}

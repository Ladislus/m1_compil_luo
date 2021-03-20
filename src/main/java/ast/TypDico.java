package ast;

import java.util.Objects;

public class TypDico extends Type {

    private final Type type;

    public TypDico(Position position, Type type) {
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
        TypDico typDico = (TypDico) o;
        return type.equals(typDico.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}

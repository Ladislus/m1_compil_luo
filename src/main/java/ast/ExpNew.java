package ast;

import java.util.Objects;

public class ExpNew extends Expression{
    private final Type type;
    private final Expression argument;

    public Type getType() {
        return type;
    }

    public Expression getArgument() {
        return argument;
    }

    public ExpNew(Position position, Type type, Expression argument) {
        this.position = position;
        this.type = type;
        this.argument = argument;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpNew expNew = (ExpNew) o;
        return type.equals(expNew.type) && argument.equals(expNew.argument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, argument);
    }
}

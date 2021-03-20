package ast;

import java.util.List;
import java.util.Objects;

public class ExpNew extends Expression{
    private final Type type;
    private final List<Expression> arguments;

    public Type getType() {
        return type;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public ExpNew(Position position, Type type, List<Expression> arguments) {
        this.position = position;
        this.type = type;
        this.arguments = arguments;
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
        return type.equals(expNew.type) && arguments.equals(expNew.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, arguments);
    }
}

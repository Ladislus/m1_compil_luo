package ast;

import java.util.List;

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
}

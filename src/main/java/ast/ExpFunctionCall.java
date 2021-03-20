package ast;

import java.util.List;
import java.util.Objects;

public class ExpFunctionCall extends Expression {

    private final String name;
    private final List<Expression> arguments;

    public ExpFunctionCall(Position position, String function, List<Expression> arguments) {
        this.position = position;
        this.name = function;
        this.arguments = arguments;
    }


    public String getName() {
        return this.name;
    }

    public List<Expression> getArguments() {
        return this.arguments;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpFunctionCall that = (ExpFunctionCall) o;
        return name.equals(that.name) && arguments.equals(that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, arguments);
    }
}

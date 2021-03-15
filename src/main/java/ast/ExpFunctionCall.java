package ast;

import java.util.List;

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
}

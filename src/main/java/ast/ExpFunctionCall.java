package ast;

import java.util.List;

public class ExpFunctionCall extends Expression {

    private final String function;
    private final List<Expression> args;

    public ExpFunctionCall(String function, List<Expression> args) {
        this.function = function;
        this.args = args;
    }


    public String getFunction() {
        return this.function;
    }

    public List<Expression> getArgs() {
        return this.args;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

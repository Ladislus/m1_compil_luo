package ast;

import java.util.Optional;

public class Declaration extends Node{

    private Type type;
    private String variable;
    private Optional<Expression> expression;

    public Declaration(Type type, String variable) {
        this.type = type;
        this.variable = variable;
        this.expression = Optional.empty();
    }

    public Declaration(Type type, String variable, Expression expression) {
        this.type = type;
        this.variable = variable;
        this.expression = Optional.of(expression);
    }

    public Type getType() {
        return type;
    }

    public String getVariable() {
        return variable;
    }

    public Optional<Expression> getExpression() {
        return expression;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

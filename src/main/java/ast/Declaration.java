package ast;

import java.util.Objects;
import java.util.Optional;

public class Declaration extends Node{

    private Type type;
    private String variable;
    private final Optional<Expression> expression;

    public Declaration(Declaration declaration){
        this.position = declaration.position;
        this.type = declaration.type;
        this.variable = declaration.variable;
        this.expression = declaration.expression;
    }

    public Declaration(Position position, Type type, String variable) {
        this.position = position;
        this.type = type;
        this.variable = variable;
        this.expression = Optional.empty();
    }

    public Declaration(Position position, Type type, String variable, Expression expression) {
        this.position = position;
        this.type = type;
        this.variable = variable;
        this.expression = Optional.of(expression);
    }

    public Declaration(Position position, Type type, String variable, Optional<Expression> expression) {
        this.position = position;
        this.type = type;
        this.variable = variable;
        this.expression = expression;
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
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Declaration that = (Declaration) o;
        return type.equals(that.type) && variable.equals(that.variable) && expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, variable, expression);
    }
}

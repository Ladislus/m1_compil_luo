package ast;

import java.util.Objects;

public class InsExpression extends Instruction {

    private final Expression expression;

    public InsExpression(Position position, Expression expression) {
        this.position = position;
        this.expression = expression;
    }

    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsExpression that = (InsExpression) o;
        return expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }
}

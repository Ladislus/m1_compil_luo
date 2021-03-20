package ast;

import java.util.Objects;

public class InsReturn extends Instruction {

    private final Expression expression;

    public InsReturn(Position position, Expression expression) {
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
        InsReturn insReturn = (InsReturn) o;
        return expression.equals(insReturn.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }
}

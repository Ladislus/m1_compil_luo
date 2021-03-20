package ast;

import java.util.Objects;

public class ExpVariable extends Expression {

    private final String variable;

    public ExpVariable(Position position, String variable) {
        this.position = position;
        this.variable = variable;
    }

    public String getVariable() {
        return this.variable;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpVariable that = (ExpVariable) o;
        return variable.equals(that.variable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable);
    }
}

package ast;

import java.util.Objects;

public class InsAssign extends Instruction {

    private final Expression lValue;
    private final Expression expression;
    private final EnumAssignOp operation;

    public InsAssign(Position position, Expression lValue, EnumAssignOp operation, Expression expression) {
        this.position = position;
        this.lValue = lValue;
        this.expression = expression;
        this.operation = operation;
    }

    public Expression getlValue() {
        return this.lValue;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public EnumAssignOp getOperation() {
        return this.operation;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsAssign insAssign = (InsAssign) o;
        return lValue.equals(insAssign.lValue) && expression.equals(insAssign.expression) && operation == insAssign.operation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lValue, expression, operation);
    }
}

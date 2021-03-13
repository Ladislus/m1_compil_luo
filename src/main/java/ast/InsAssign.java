package ast;

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
}

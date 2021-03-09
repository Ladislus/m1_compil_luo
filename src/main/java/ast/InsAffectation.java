package ast;

public class InsAffectation extends Instruction {

    private final Expression Expression1;
    private final Expression Expression2;
    private final EnumEqualOp operation;

    public InsAffectation(Expression expression1, Expression expression2, EnumEqualOp operation) {
        this.Expression1 = expression1;
        this.Expression2 = expression2;
        this.operation = operation;
    }

    public Expression getExpression1() {
        return this.Expression1;
    }

    public Expression getExpression2() {
        return this.Expression2;
    }

    public EnumEqualOp getOperation() {
        return this.operation;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

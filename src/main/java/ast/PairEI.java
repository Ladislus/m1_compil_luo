package ast;

public class PairEI {
    private final Expression expression;
    private final Instruction instruction;

    public PairEI(Expression expression, Instruction instruction) {
        this.expression = expression;
        this.instruction = instruction;
    }

    public Expression getExpression() {
        return expression;
    }

    public Instruction getInstruction() {
        return instruction;
    }


}

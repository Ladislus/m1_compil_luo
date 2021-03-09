package ast;

public class InsWhile extends Instruction {

    private final Instruction body;
    private final Expression condition;
    private final boolean doWhile;

    public InsWhile(Expression condition, Instruction body, boolean doWhile){
        this.condition = condition;
        this.body = body;
        this.doWhile = doWhile;
    }

    public Expression getCondition() {return this.condition; }

    public Instruction getBody() {return  this.body; }

    public boolean getDoWhile() {return this.doWhile; }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

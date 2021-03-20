package ast;

import java.util.Objects;

public class InsWhile extends Instruction {

    private final Instruction body;
    private final Expression condition;
    private final boolean doWhile;

    public static InsWhile insDoWhile(Position position, Expression condition, Instruction body) {
        return new InsWhile(position, condition, body, true);
    }

    public static InsWhile insWhile(Position position, Expression condition, Instruction body) {
        return new InsWhile(position, condition, body, false);
    }

    private InsWhile(Position position, Expression condition, Instruction body, boolean doWhile){
        this.position = position;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsWhile insWhile = (InsWhile) o;
        return doWhile == insWhile.doWhile && body.equals(insWhile.body) && condition.equals(insWhile.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, condition, doWhile);
    }
}

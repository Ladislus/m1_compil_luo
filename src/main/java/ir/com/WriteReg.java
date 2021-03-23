package ir.com;

import ir.Register;
import ir.expr.Expression;

public class WriteReg extends Command
{
    private Register reg;
    private Expression exp;

    public Register getReg() {
        return reg;
    }

    public Expression getExp() {
        return exp;
    }

    public WriteReg(Register reg, Expression exp) {
        this.reg = reg;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return reg + " := " + exp;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

package ir.expr;

import ir.Register;
import ir.Type;

public class ReadReg extends Expression {
    private ir.Register register;

    public ReadReg(ir.Register register) {
        this.register = register;
    }

    @Override
    public Type getType() {
        return register.getType();
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public String toString() {
        return register.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

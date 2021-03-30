package ir.com;

import ir.Frame;
import ir.Register;
import ir.expr.Expression;

import java.util.List;

public class FunCall extends Command
{
    private Register register;
    private Frame frame;
    private List<Expression> arguments;

    public Frame getFrame() {
        return frame;
    }

    public Register getRegister() {
        return register;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public FunCall(Register register, Frame frame, List<Expression> arguments) {
        this.register = register;
        this.frame = frame;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return register + " := call " + frame.getEntryPoint().toString().replace(":","") + arguments;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

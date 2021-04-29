package ir.com;

import ir.Frame;
import ir.expr.Expression;

import java.util.List;

public class ProcCall extends Command {
    private ir.Frame frame;
    private List<Expression> arguments;

    public Frame getFrame() {
        return frame;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public ProcCall(Frame frame, List<Expression> arguments) {
        this.frame = frame;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "Call " + frame.getEntryPoint() + " " + arguments;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

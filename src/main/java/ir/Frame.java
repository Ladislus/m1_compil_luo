package ir;

import ir.com.Label;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Frame
{
    private Label entryPoint;
    private Label exitPoint;
    private List<Register> parameters;
    private List<Boolean> passedByRef;
    private Optional<Register> result;
    private List<Register> locals;
    private int size;

    public Label getEntryPoint() {
        return entryPoint;
    }

    public Label getExitPoint() {
        return exitPoint;
    }

    public List<Register> getParameters() {
        return parameters;
    }

    public List<Boolean> getPassedByRef() {
        return passedByRef;
    }

    public Optional<Register> getResult() {
        return result;
    }

    public List<Register> getLocals() {
        return locals;
    }

    public void addLocal(Register register) {
        this.locals.add(register);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Frame{" +
                "entryPoint=" + entryPoint +
                ", exitPoint=" + exitPoint +
                ", parameters=" + parameters +
                ", passedByRef=" + passedByRef +
                ", result=" + result +
                ", locals=" + locals +
                ", size=" + size +
                '}';
    }

    private Frame(Label entryPoint, Label exitPoint, List<Register> parameters,
                  List<Boolean> passedByRef, Optional<Register> result, int size) {
        this.entryPoint = entryPoint;
        this.exitPoint = exitPoint;
        this.parameters = parameters;
        this.passedByRef = passedByRef;
        this.result = result;
        this.size = size;
        this.locals = new LinkedList<>();
    }

    public Frame(Label entryPoint, Label exitPoint, List<Register> parameters, List<Boolean> passedByRef, Register result) {
        this(entryPoint, exitPoint, parameters, passedByRef, Optional.of(result), 0);
    }

    public Frame(Label entryPoint, Label exitPoint, List<Register> parameters, List<Boolean> passedByRef) {
        this(entryPoint, exitPoint, parameters, passedByRef, Optional.empty(), 0);
    }

    public Frame(Label entryPoint, Label exitPoint, List<Register> parameters, Register result) {
        this(entryPoint, exitPoint, parameters, new ArrayList<>(), Optional.of(result), 0);
    }

    public Frame(Label entryPoint, Label exitPoint, List<Register> parameters) {
        this(entryPoint, exitPoint, parameters, new ArrayList<>(), Optional.empty(), 0);
    }
}

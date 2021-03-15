package ast;

public class ExpBoolean extends Expression{

    private final boolean value;

    public ExpBoolean(Position position, boolean value) {
        this.position = position;
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

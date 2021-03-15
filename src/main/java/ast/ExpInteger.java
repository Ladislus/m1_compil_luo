package ast;

public class ExpInteger extends Expression {

    private final int value;

    public ExpInteger(Position position, int value) {
        this.position = position;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

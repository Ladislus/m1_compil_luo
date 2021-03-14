package ast;

public class ExpString extends Expression {

    private final String value;

    public ExpString(Position position, String value) {
        this.position = position;
        this.value = value;
    }

    public String getValue() { return this.value; }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

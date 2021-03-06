package ast;

public class ExpString extends Expression {

    private final String value;

    public ExpString(String value) {
        this.value = value;
    }

    public String getValue() { return this.value; }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

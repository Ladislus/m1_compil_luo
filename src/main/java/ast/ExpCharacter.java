package ast;

public class ExpCharacter extends Expression {

    private final char value;

    public ExpCharacter(char value) {
        this.value = value;
    }

    public char getValue() {
        return this.value;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

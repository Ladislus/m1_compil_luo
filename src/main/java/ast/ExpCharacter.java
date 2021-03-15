package ast;

public class ExpCharacter extends Expression {

    private final char value;

    public ExpCharacter(Position position, char value) {
        this.position = position;
        this.value = value;
    }

    public char getValue() {
        return this.value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

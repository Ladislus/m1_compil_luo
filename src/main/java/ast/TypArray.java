package ast;

public class TypArray extends Type {

    private Type type;

    public TypArray(Position position, Type type) {
        this.position = position;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

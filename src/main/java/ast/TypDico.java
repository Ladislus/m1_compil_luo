package ast;

public class TypDico extends Type{
    private Type type;


    public TypDico(Type type) {
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

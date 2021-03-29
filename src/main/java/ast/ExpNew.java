package ast;

public class ExpNew extends Expression{
    private final Type type;
    private final Expression argument;

    public Type getType() {
        return type;
    }

    public Expression getArgument() {
        return argument;
    }

    public ExpNew(Position position, Type type, Expression argument) {
        this.position = position;
        this.type = type;
        this.argument = argument;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

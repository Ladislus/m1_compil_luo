package ast;

public class InstForeach extends Instruction{

    private final Expression collection;
    private final Type type;
    private final Expression identifier;
    private final Instruction body;

    public InstForeach(Type type, Expression identifier, Expression collection, Instruction body){
        this.type = type;
        this.collection = collection;
        this.identifier = identifier;
        this.body = body;
    }

    public Expression getCollection() {
        return this.collection;
    }

    public Expression getIdentifier() {
        return this.identifier;
    }

    public Instruction getBody() {
        return this.body;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

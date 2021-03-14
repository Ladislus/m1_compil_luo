package ast;

public class InstForeach extends Instruction{

    private final Expression collection;
    private final Type type;
    private final String identifier;
    private final Instruction body;

    public InstForeach(Position position, Type type, String identifier, Expression collection, Instruction body){
        this.position = position;
        this.type = type;
        this.collection = collection;
        this.identifier = identifier;
        this.body = body;
    }

    public Expression getCollection() {
        return this.collection;
    }

    public String getIdentifier() {
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

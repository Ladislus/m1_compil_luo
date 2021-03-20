package ast;

import java.util.Objects;

public class InstForeach extends Instruction{

    private final Declaration declaration;
    private final Expression collection;
    private final Instruction body;

    public InstForeach(Position position, Type type, String identifier, Expression collection, Instruction body){
        this.position = position;
        this.collection = collection;
        this.declaration = new Declaration(position, type, identifier);
        this.body = body;
    }

    public InstForeach(Position position, Declaration declaration, Expression collection, Instruction body) {
        this.position = position;
        this.declaration = declaration;
        this.collection = collection;
        this.body = body;
    }

    public Expression getCollection() {
        return this.collection;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public Instruction getBody() {
        return this.body;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstForeach that = (InstForeach) o;
        return declaration.equals(that.declaration) && collection.equals(that.collection) && body.equals(that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(declaration, collection, body);
    }
}

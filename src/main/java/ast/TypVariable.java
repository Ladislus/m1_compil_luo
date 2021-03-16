package ast;

public class TypVariable extends Type {
    private String name;

    public TypVariable(Position position, String name) {
        this.position = position;
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

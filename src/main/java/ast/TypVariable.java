package ast;
public class TypVariable extends Type {
    private String name;

    public TypVariable(String name) {
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

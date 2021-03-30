package ast;

public class TypVariable extends Type {

    private final String name;

    public TypVariable(Position position, String name) {
        this.position = position;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypVariable that = (TypVariable) o;
        return name.equals(that.name);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

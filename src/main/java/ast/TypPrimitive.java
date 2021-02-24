package ast;

public class TypPrimitive extends Type {

    private String name;

    public TypPrimitive(EnumPrimitiveType primitiveType) {
        this.name = primitiveType.name();
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
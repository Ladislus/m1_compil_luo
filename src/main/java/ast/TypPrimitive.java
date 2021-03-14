package ast;

public class TypPrimitive extends Type {

    private EnumPrimitiveType type;

    public TypPrimitive(Position position, EnumPrimitiveType primitiveType) {
        this.position = position;
        this.type = primitiveType;
    }

    public EnumPrimitiveType getType() {
        return type;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
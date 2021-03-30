package ast;

public class TypPrimitive extends Type {

    private final EnumPrimitiveType type;

    public TypPrimitive(Position position, EnumPrimitiveType primitiveType) {
        this.position = position;
        this.type = primitiveType;
    }

    public EnumPrimitiveType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypPrimitive that = (TypPrimitive) o;
        return type == that.type;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
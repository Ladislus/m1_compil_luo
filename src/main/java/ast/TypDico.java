package ast;

public class TypDico extends Type{
    private Type keyType;
    private Type elementType;
    private TypRecord pairType;

    public TypDico(TypRecord typRecord) {
        this.keyType = typRecord.getAttributeTypes().get(0);
        this.elementType = typRecord.getAttributeTypes().get(1);
        this.pairType = pairType;
    }

    public Type getKeyType() {
        return keyType;
    }
    public Type getElementType() {
        return elementType;
    }
    public TypRecord getPairType() { return pairType; }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

package ast;

public class TypInteger extends Type {

    public String getType() {
        return EnumType.INT.name();
    }
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
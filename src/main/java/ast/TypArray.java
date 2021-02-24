package ast;

public class TypArray extends Type {

    public String getType(){ return EnumType.ARRAY.name(); }

    @Override
    public <T> T accept(Visitor<T> visitor){
        return visitor.visit(this);
    }
}

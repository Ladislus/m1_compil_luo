package ast;

public class TypBoolean extends Type{
    public String getType(){ return EnumType.BOOL.name(); }

    public TypBoolean(){}

    @Override
    public <T> T accept(Visitor<T> visitor){
        return visitor.visit(this);
    }
}

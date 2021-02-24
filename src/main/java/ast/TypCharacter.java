package ast;

public class TypCharacter extends Type{
    public String getType(){ return EnumType.CHAR.name(); }

    public TypCharacter(){}

    @Override
    public <T> T accept(Visitor<T> visitor){
        return visitor.visit(this);
    }
}

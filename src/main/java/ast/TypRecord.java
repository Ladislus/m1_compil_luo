package ast;

public class TypRecord extends Type{
    private final String structName;

    public String getType(){ return structName; }
    public TypRecord(String structName){
        this.structName = structName;
    }
    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

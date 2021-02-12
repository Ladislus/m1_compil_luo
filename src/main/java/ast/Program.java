package ast;

public class Program implements Node{
    // TO COMPLETE

    public <T> T accept(Visitor<T> visitor){
        return visitor.visit(this);
    }
}

package ast;

public class Program extends Node{
    // TO COMPLETE

    public <T> T accept(Visitor<T> visitor){
        return visitor.visit(this);
    }
}

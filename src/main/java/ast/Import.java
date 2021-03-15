package ast;

public class Import extends Node{

    private String path;

    public Import(Position position, String path){
        this.position = position;
        this.path=path;
    }

    public String getPath() { return path; }

    @Override
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}

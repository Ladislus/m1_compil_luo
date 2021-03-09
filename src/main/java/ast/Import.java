package ast;

public class Import extends Node{

    private String path;

    public Import(String path){
        this.path=path;
    }

    public String getPath() { return path; }

    @Override
    <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}

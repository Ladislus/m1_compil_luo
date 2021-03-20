package ast;

import java.util.Objects;

public class Import extends Node{

    private final String path;

    public Import(Position position, String path){
        this.position = position;
        this.path=path;
    }

    public String getPath() { return path; }

    @Override
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Import anImport = (Import) o;
        return path.equals(anImport.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}

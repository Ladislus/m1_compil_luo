package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TypeDefinition extends Node {

    List<Declaration> declarations;
    String name;

    public TypeDefinition(Position position, String name, Declaration declaration){
        this.position = position;
        this.name=name;
        List<Declaration> listDeclaration = new ArrayList<>();
        listDeclaration.add(declaration);
        this.declarations = listDeclaration;
    }

    public TypeDefinition(Position position, String name, List<Declaration> declarationList){
        this.name=name;
        this.position = position;
        this.declarations = declarationList;
    }

    
    public List<Declaration> getDeclarations() { return declarations; }
    public String getName(){ return this.name;}

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TypeDefinition that = (TypeDefinition) o;
        return declarations.equals(that.declarations) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), declarations, name);
    }
}

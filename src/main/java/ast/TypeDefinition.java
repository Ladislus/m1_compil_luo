package ast;

import java.util.ArrayList;
import java.util.List;

public class TypeDefinition extends Node{

    List<Declaration> declarations;
    String name;

    public TypeDefinition(Position position, String name, Declaration declaration){
        this.position = position;
        this.name=name;
        List<Declaration> listDeclaration = new ArrayList<Declaration>();
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
}

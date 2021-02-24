package ast;

import org.antlr.v4.codegen.model.decl.Decl;

import java.util.ArrayList;
import java.util.List;

public class TypeDefinition extends Node{

    List<Declaration> declarations;
    String name;

    public TypeDefinition(String name, Declaration declaration){
        this.name=name;
        List<Declaration> listDeclaration = new ArrayList<Declaration>();
        listDeclaration.add(declaration);
        this.declarations = listDeclaration;
    }

    public TypeDefinition(String name, List<Declaration> declarationList){
        this.name=name;
        this.declarations = declarationList;
    }

    public List<Declaration> getDeclarations() { return declarations; }
    public String getName(){ return this.name;}

    @Override
    <T> T accept(Visitor<T> visitor) {
        return null;
    }
}

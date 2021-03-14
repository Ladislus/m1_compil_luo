package ast;
import java.util.ArrayList;
import java.util.List;


public class Program extends Node{

    private List<Import> imports;
    private List<GlobalDeclaration> globalDeclarations;
    private List<TypeDefinition> typeDefinitions;
    private List<Function> functions;

    public Program(Position position) {
        this(position,
          new ArrayList<>(), new ArrayList<>(),
          new ArrayList<>(), new ArrayList<>());
    }

    public Program(Position position,
                   List<Import> imports,
                   List<GlobalDeclaration> globalDeclarations,
                   List<TypeDefinition> typeDefinitions,
                   List<Function> functions) {
        this.position = position;
        this.imports = imports;
        this.globalDeclarations = globalDeclarations;
        this.typeDefinitions = typeDefinitions;
        this.functions = functions;
    }

    public List<Import> getImports() {
        return imports;
    }

    public List<GlobalDeclaration> getGlobalDeclarations() {
        return globalDeclarations;
    }

    public List<TypeDefinition> getTypeDefinitions() {
        return typeDefinitions;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public <T> T accept(Visitor<T> visitor){
        return visitor.visit(this);
    }
}

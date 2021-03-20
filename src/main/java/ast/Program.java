package ast;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Program extends Node{

    private final List<Import> imports;
    private final List<GlobalDeclaration> globalDeclarations;
    private final List<TypeDefinition> typeDefinitions;
    private final List<Function> functions;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Program program = (Program) o;
        return imports.equals(program.imports) && globalDeclarations.equals(program.globalDeclarations) && typeDefinitions.equals(program.typeDefinitions) && functions.equals(program.functions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), imports, globalDeclarations, typeDefinitions, functions);
    }
}

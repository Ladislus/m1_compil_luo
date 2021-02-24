package ast;

public interface VisitorGlo<T> {

    T visit(Program program);
    T visit(GlobalDeclaration globalDeclaration);
    T visit(Import imports);
    T visit(Function function);
    T visit(TypeDefinition typeDefinition);

}

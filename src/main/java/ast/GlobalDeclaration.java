package ast;

public class GlobalDeclaration extends Declaration {
    private EnumVisibility visibility;

    public GlobalDeclaration(Type type, String variable) {
        super(type, variable);
        this.visibility = EnumVisibility.PUBLIC;
    }

    public GlobalDeclaration(Type type, String variable, Expression expression) {
        super(type, variable, expression);
        this.visibility = EnumVisibility.PUBLIC;
    }

    public GlobalDeclaration(EnumVisibility visibility, Type type, String variable) {
        super(type, variable);
        this.visibility = visibility;
    }

    public GlobalDeclaration(EnumVisibility visibility, Type type, String variable, Expression expression) {
        super(type, variable, expression);
        this.visibility = visibility;
    }

    public EnumVisibility getVisibility() {
        return visibility;
    }
}

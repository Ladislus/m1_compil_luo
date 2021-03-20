package ast;

import java.util.Objects;
import java.util.Optional;

public class GlobalDeclaration extends Declaration {

    private final EnumVisibility visibility;

    public GlobalDeclaration(Position position, Type type, String variable) {
        super(position, type, variable);
        this.visibility = EnumVisibility.PUBLIC;
    }

    public GlobalDeclaration(Position position, Type type, String variable, Expression expression) {
        super(position, type, variable, expression);
        this.visibility = EnumVisibility.PUBLIC;
    }

    public GlobalDeclaration(EnumVisibility visibility, Type type, String variable) {
        super(type.position, type, variable);
        this.visibility = visibility;
    }

    public GlobalDeclaration(Position position, EnumVisibility visibility, Type type, String variable, Expression expression) {
        super(position, type, variable, expression);
        this.visibility = visibility;
    }

    public GlobalDeclaration(Position position, EnumVisibility visibility, Type type, String variable, Optional<Expression> expression) {
        super(position, type, variable, expression);
        this.visibility = visibility;
    }

    public EnumVisibility getVisibility() {
        return visibility;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GlobalDeclaration that = (GlobalDeclaration) o;
        return visibility == that.visibility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), visibility);
    }
}

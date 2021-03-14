package ast;

import javafx.geometry.Pos;

import javax.crypto.spec.PSource;
import java.util.Optional;

public class GlobalDeclaration extends Declaration {
    private EnumVisibility visibility;

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
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

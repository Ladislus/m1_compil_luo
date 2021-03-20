package ast;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Function extends Node{

    private final EnumVisibility visibility;
    private final String name;
    private final List<Declaration> parameters;
    private final InsBlock body;
    private final Optional<Type> return_type;

    public Function(Position position, EnumVisibility visibility, String name, List<Declaration> parameters, InsBlock body, Optional<Type> return_type) {
        this.position = position;
        this.visibility = visibility;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
        this.return_type = return_type;
    }

    public EnumVisibility getVisibility() {
        return visibility;
    }

    public String getName() {
        return name;
    }

    public List<Declaration> getParameters() {
        return parameters;
    }

    public InsBlock getBody() {
        return body;
    }

    public Optional<Type> getReturn_type() {
        return return_type;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Function function = (Function) o;
        return visibility == function.visibility && name.equals(function.name) && parameters.equals(function.parameters) && body.equals(function.body) && return_type.equals(function.return_type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visibility, name, parameters, body, return_type);
    }
}

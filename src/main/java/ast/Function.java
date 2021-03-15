package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Function extends Node{

    private EnumVisibility visibility;
    private String name;
    private List<Declaration> parameters;
    private InsBlock body;
    private Optional<Type> return_type;

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
}

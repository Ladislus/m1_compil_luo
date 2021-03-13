package ast;

import java.util.List;

public class ExpEnum extends Expression {

    private final List<Expression> elements;

    public ExpEnum(Position position, List<Expression> elements) {
        this.position = position;
        this.elements = elements;
    }

    public List<Expression> getElements() {
        return this.elements;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

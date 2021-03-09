package ast;

import java.util.List;

public class ExpEnum extends Expression {

    private final List<Expression> elements;

    public ExpEnum(List<Expression> elements) {
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
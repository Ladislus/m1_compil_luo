package ast;

import java.util.List;

public class ExpArrayEnum extends Expression {

    private final List<Expression> elements;

    public ExpArrayEnum(Position position, List<Expression> elements) {
        this.position = position;
        this.elements = elements;
    }

    public List<Expression> getElements() {
        return this.elements;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

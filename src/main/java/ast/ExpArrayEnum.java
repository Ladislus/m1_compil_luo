package ast;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpArrayEnum that = (ExpArrayEnum) o;
        return elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}

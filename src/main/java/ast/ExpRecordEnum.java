package ast;

import support.Pair;

import java.util.List;
import java.util.Objects;

public class ExpRecordEnum extends Expression {

    private final List<Pair<String,Expression>> fieldValues;

    public ExpRecordEnum(Position position, List<Pair<String, Expression>> fieldValues) {
        this.position = position;
        this.fieldValues = fieldValues;
    }

    public List<Pair<String, Expression>> getFieldValues() {
        return fieldValues;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpRecordEnum that = (ExpRecordEnum) o;
        return fieldValues.equals(that.fieldValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldValues);
    }
}

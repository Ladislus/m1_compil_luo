package ast;

import support.Pair;

import java.util.List;

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
}

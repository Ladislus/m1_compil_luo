package ast;

import java.util.Objects;

public class ExpRecordAccess extends Expression {

    private final Expression record;
    private final String field;

    public ExpRecordAccess(Position position, Expression record, String field) {
        this.position = position;
        this.record = record;
        this.field = field;
    }

    public Expression getRecord() {
        return this.record;
    }

    public String getField() {
        return this.field;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpRecordAccess that = (ExpRecordAccess) o;
        return record.equals(that.record) && field.equals(that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(record, field);
    }
}

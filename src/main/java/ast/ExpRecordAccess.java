package ast;

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
}

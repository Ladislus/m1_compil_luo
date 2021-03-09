package ast;

public class ExpRecord extends Expression {

    private final Expression record;
    private final String field;

    public ExpRecord(Expression record, String field) {
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
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

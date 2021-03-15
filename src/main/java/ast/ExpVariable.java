package ast;

public class ExpVariable extends Expression {

    private final String variable;

    public ExpVariable(Position position, String variable) {
        this.position = position;
        this.variable = variable;
    }

    public String getVariable() {
        return this.variable;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

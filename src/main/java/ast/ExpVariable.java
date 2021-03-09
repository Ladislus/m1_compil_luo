package ast;

public class ExpVariable extends Expression {

    private final String variable;

    public ExpVariable(String variable) {
        this.variable = variable;
    }

    public String getVariable() {
        return this.variable;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

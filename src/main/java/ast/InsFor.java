package ast;

import java.util.Objects;

public class InsFor extends Instruction {

    private final Declaration declaration;
    private final Expression range;
    private final Expression step;
    private final Instruction body;

    public InsFor(Position position, Declaration declaration, Expression range, Expression step, Instruction body) {
        this.position = position;
        this.declaration = declaration;
        this.range = range;
        this.step = step;
        this.body = body;
    }

    public Declaration getDeclaration() {
        return this.declaration;
    }

    public Expression getRange() {
        return this.range;
    }

    public Expression getStep() {
        return this.step;
    }

    public Instruction getBody() {
        return this.body;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsFor insFor = (InsFor) o;
        return declaration.equals(insFor.declaration) && range.equals(insFor.range) && step.equals(insFor.step) && body.equals(insFor.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(declaration, range, step, body);
    }
}

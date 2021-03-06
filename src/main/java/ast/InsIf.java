package ast;

import support.Pair;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InsIf extends Instruction{

    private final Expression condition;
    private final Instruction body;
    private final List<Pair<Expression,Instruction>> elseif;
    private final Optional<Instruction> bodyElse;

    public InsIf(Position position, Expression condition, Instruction body, List<Pair<Expression,Instruction>> elseif) {
        this.position = position;
        this.condition = condition;
        this.body = body;
        this.elseif = elseif;
        this.bodyElse = Optional.empty();
    }

    public InsIf(Position position, Expression condition, Instruction body, List<Pair<Expression,Instruction>> elseif, Instruction bodyElse) {
        this.position = position;
        this.condition = condition;
        this.body = body;
        this.elseif = elseif;
        this.bodyElse = Optional.of(bodyElse);
    }

    public InsIf(Position position, Expression condition, Instruction body, List<Pair<Expression,Instruction>> elseif, Optional<Instruction> bodyElse) {
        this.position = position;
        this.condition = condition;
        this.body = body;
        this.elseif = elseif;
        this.bodyElse = bodyElse;
    }

    public Expression getCondition() {
        return this.condition;
    }

    public Instruction getBody() {
        return this.body;
    }

    public List<Pair<Expression,Instruction>> getElseif() {
        return this.elseif;
    }

    public Optional<Instruction> getBodyElse() {
        return this.bodyElse;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsIf insIf = (InsIf) o;
        return condition.equals(insIf.condition) && body.equals(insIf.body) && elseif.equals(insIf.elseif) && bodyElse.equals(insIf.bodyElse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, body, elseif, bodyElse);
    }
}

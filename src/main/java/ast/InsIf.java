package ast;

import java.util.List;
import java.util.Optional;

public class InsIf extends Instruction{

    private final Expression condition;
    private final Instruction body;
    private final List<PairEI> elseif;
    private final Optional<Instruction> bodyElse;

    public InsIf(Expression condition, Instruction body, List<PairEI> elseif) {
        this.condition = condition;
        this.body = body;
        this.elseif = elseif;
        this.bodyElse = Optional.empty();
    }

    public InsIf(Expression condition, Instruction body, List<PairEI> elseif, Instruction bodyElse) {
        this.condition = condition;
        this.body = body;
        this.elseif = elseif;
        this.bodyElse = Optional.of(bodyElse);
    }

    public Expression getCondition() {
        return this.condition;
    }

    public Instruction getBody() {
        return this.body;
    }

    public List<PairEI> getElseif() {
        return this.elseif;
    }

    public Optional<Instruction> getBodyElse() {
        return this.bodyElse;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

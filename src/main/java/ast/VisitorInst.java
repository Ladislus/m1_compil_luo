package ast;

public interface VisitorInst<T> {
    // SHOULD NOT BE IN THE VISITOR ! T visit(Instruction instruction);
    T visit(InsFor instruction);
    T visit(InsWhile instruction);
    T visit(InstForeach instruction);
    T visit(InsIf instruction);
    T visit(InsAssign instruction);
    T visit(InsBlock instruction);
    T visit(InsBreak instruction);
    T visit(InsExpression instruction);
    T visit(InsReturn instruction);
}

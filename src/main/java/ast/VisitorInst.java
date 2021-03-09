package ast;

public interface VisitorInst<T> {
    T visit(Instruction instruction);
    T visit(InsFor instruction);
    T visit(InsWhile instruction);
    T visit(InstForeach instruction);
    T visit(InsIf instruction);
    T visit(InsAffectation instruction);
    T visit(InsBlock instruction);
    T visit(InsBreak instruction);
    T visit(InsExpression instruction);
}

package ast;

import java.util.ArrayList;

public class VisitorCopy implements Visitor<Node>{


    // ##########################
    // #    Block Expression    #
    // ##########################
    /**
     * @author Ladislas WALCAK
     * @author Thomas QUETIER
     * @author Corentin HERVOCHON
     * @author Martin GUERRAUD
     */
    @Override
    public Node visit(ExpPreUnaryOperation operation) {
        return new ExpPreUnaryOperation(operation.getExpression(), operation.getOperator()).getExpression().accept(this);
    }

    @Override
    public Node visit(ExpPostUnaryOperation operation) {
        return new ExpPostUnaryOperation(operation.getExpression(), operation.getOperator()).getExpression().accept(this);
    }

    @Override
    public Node visit(ExpBinaryOperation operation) {
        ExpBinaryOperation no = new ExpBinaryOperation(operation.getLeft(), operation.getOperator(), operation.getRight());
        no.getLeft().accept(this);
        return no.getRight().accept(this);
    }

    @Override
    public Node visit(ExpBoolean bool) {
        return null;
    }

    @Override
    public Node visit(ExpCharacter character) {
        return null;
    }

    @Override
    public Node visit(ExpInteger integer) {
        return null;
    }

    @Override
    public Node visit(ExpVariable variable) {
        return null;
    }

    @Override
    public Node visit(ExpRecordAccess record) {
        return new ExpRecordAccess(record.getRecord(), new String(record.getField())).getRecord().accept(this);
    }

    @Override
    public Node visit(ExpArrayAccess array) {
        ExpArrayAccess na = new ExpArrayAccess(array.getArray(), array.getIndex());
        na.getArray().accept(this);
        return na.getIndex().accept(this);
    }

    @Override
    public Node visit(ExpEnum enumeration) {
        ExpEnum ne = new ExpEnum(new ArrayList<>(enumeration.getElements()));
        Node curr = null;
        for (Expression exp : ne.getElements()) curr = exp.accept(this);
        return curr;
    }

    @Override
    public Node visit(ExpTuple tuple) {
        ExpTuple nt = new ExpTuple(tuple.getFirst(), tuple.getSecond());
        nt.getFirst().accept(this);
        return nt.getSecond().accept(this);
    }

    @Override
    public Node visit(ExpFunctionCall function) {
        ExpFunctionCall nf = new ExpFunctionCall(new String(function.getFunction()), new ArrayList<>(function.getArgs()));
        Node curr = null;
        for (Expression exp : nf.getArgs()) curr = exp.accept(this);
        return curr;
    }

    @Override
    public Node visit(ExpString string) {
        return null;
    }
    // ##############################
    // #    Fin Block Expression    #
    // ##############################


    /**
     *Expression de types
     * @author GUINDO Mouctar Ousseini
     * @author GBOHO Thierry
     * @author HEBRAS Jerome
     * @author GUINGOUAIN Nicolas
     *
     *
     */
    @Override
    public Node visit(TypPrimitive typPrimitive) {
        return null;
    }

    @Override
    public Node visit(TypDico typDictionary) {
        return  new TypDico(typDictionary.getType()).getType().accept(this);
    }

    @Override
    public Node visit(TypVariable typRecord) {
        return null;
    }

    @Override
    public Node visit(TypArray typArray) {
        return new TypArray(typArray.getType()).getType().accept(this);
    }


}

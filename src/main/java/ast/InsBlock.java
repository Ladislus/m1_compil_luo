package ast;

import java.util.ArrayList;

public class InsBlock extends Instruction {

    private final ArrayList<Declaration> declarations;
    private final ArrayList<Instruction> listBody;

    public InsBlock(ArrayList<Declaration> declarations, ArrayList<Instruction> listBody) {
        this.declarations = declarations;
        this.listBody = listBody;
    }

    public ArrayList<Declaration> getDeclarations() {
        return this.declarations;
    }

    public ArrayList<Instruction> getListBody() {
        return this.listBody;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

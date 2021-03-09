package ast;

public class VisitorCopy<Node> implements Visitor<Node>{


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

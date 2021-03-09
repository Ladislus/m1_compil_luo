package ast;

public interface VisitorTyp<T> {
    T visit(TypPrimitive typPrimitive);
    T visit(TypDico typDictionary);
    T visit(TypVariable typRecord);
    T visit(TypArray typArray);
}

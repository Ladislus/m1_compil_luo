package ast;

public interface VisitorTyp<T> {
    T visit(TypPrimitive typPrimitive);
    T visit(TypDico typDictionary);
    T visit(TypRecord typRecord);
    T visit(TypArray typArray);
}

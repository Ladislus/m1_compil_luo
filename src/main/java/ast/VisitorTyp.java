package ast;

public interface VisitorTyp<T> {
    T visit(TypInteger typInteger);
    T visit(TypBoolean typBoolean);
    T visit(TypCharacter typCharacter);

    T visit(TypDico typDictionary);
    T visit(TypRecord typRecord);
    T visit(TypArray typArray);
}

package ast;

public abstract class Expression extends Node {
    @Override
    abstract <T> T accept(Visitor<T> visitor);
}
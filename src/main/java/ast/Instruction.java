package ast;

abstract public class Instruction extends Node{

    @Override
    abstract <T> T accept(Visitor<T> visitor);
}

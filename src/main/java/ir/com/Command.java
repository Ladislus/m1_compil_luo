package ir.com;

abstract public class Command {
    public abstract <T> T accept(Visitor<T> visitor);
}

package ir.expr;

public class Symbol extends  Expression {

    private String symbol;
    private ir.Type type;

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public Symbol(String symbol) {
        this.symbol = symbol;
        this.type = ir.Type.INT;
    }

    public Symbol(String symbol, ir.Type type) {
        this.symbol = symbol;
        this.type = type;
    }

    @Override
    public ir.Type getType() {
        return type;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    public static final Symbol INT_SIZE = new Symbol("INT_SIZE");
    public static final Symbol BYTE_SIZE = new Symbol("BYTE_SIZE");
    public static final Symbol ADDRESS_SIZE = new Symbol("ADDRESS_SIZE");
}

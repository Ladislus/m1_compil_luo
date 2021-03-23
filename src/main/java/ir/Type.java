package ir;

import ir.expr.Symbol;

public enum Type {
    BYTE {
        @Override
        public String toString() {
            return "byte";
        }

        public Symbol toSymbol() {
            return Symbol.BYTE_SIZE;
        }
    },
    INT {
        @Override
        public String toString() {
            return "int";
        }

        public Symbol toSymbol() {
            return Symbol.INT_SIZE;
        }
    },
    ADDRESS {
        @Override
        public String toString() {
            return "address";
        }

        public Symbol toSymbol() {
            return Symbol.ADDRESS_SIZE;
        }
    };

    public abstract Symbol toSymbol();
}

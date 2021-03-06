package ast;

public enum EnumUnaryOp {
    INC {
        public String toString() {
            return "++";
        }
    },
    DEC {
        public String toString() {
            return "--";
        }
    },
    NOT {
        public String toString() {
            return "!";
        }
    },
    MINUS {
        public String toString() {
            return "-";
        }
    }
}

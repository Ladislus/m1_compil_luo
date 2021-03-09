package ast;

public enum EnumEqualOp {
    MINEQUAL {
        public String toString() {return "-="; }
    },
    PLUSEQUAL {
        public String toString() {return "+="; }
    },
    DIVEQUAL {
        public String toString() {return "/="; }
    },
    MULTEQUAL {
        public String toString() {return "*="; }
    },
    EQUAL {
        public String toString() {return "="; }
    }
}

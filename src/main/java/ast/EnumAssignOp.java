package ast;

public enum EnumAssignOp {
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

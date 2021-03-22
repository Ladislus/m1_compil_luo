package ast;

public enum EnumPredefinedOp {
    INCHAR {
        @Override
        public String toString() {
            return "inputChar";
        }
    },
    INSTRING {
        @Override
        public String toString() {
            return "inputString";
        }
    },
    ININT {
        @Override
        public String toString() {
            return "inputInt";
        }
    },
    INBOOL {
        @Override
        public String toString() {
            return "inputBool";
        }
    },
    OUTCHAR {
        @Override
        public String toString() {
            return "print";
        }
    },
    OUTSTRING {
        @Override
        public String toString() {
            return "print";
        }
    },
    OUTINT {
        @Override
        public String toString() {
            return "print";
        }
    },
    OUTBOOL {
        @Override
        public String toString() {
            return "print";
        }
    },
    INTTOCHAR {
        @Override
        public String toString() {
            return "toChar";
        }
    },
    INTTOBOOL {
        @Override
        public String toString() {
            return "toBool";
        }
    },
    CHARTOINT {
        @Override
        public String toString() {
            return "toInt";
        }
    },
    BOOLTOINT {
        @Override
        public String toString() {
            return "toInt";
        }
    },
    ASSERT {
        @Override
        public String toString() {
            return "assert";
        }
    },
    FREE {
        @Override
        public String toString() {
            return "free";
        }
    },
    LENGTH {
        @Override
        public String toString() {
            return "length";
        }
    }
}

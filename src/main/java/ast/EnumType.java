package ast;

public enum EnumType {
    INT {
        public String toString() {
            return "int";
        }
    },
    CHAR {
        public String toString() {
            return "char";
        }
    },
    BOOL {
        public String toString() {
            return "bool";
        }
    },
    ARRAY {
        public String toString() {
            return "array";
        }
    }
}

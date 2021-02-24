package ast;

public enum EnumPrimitiveType {
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
    }
}

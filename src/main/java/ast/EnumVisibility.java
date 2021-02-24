package ast;

public enum EnumVisibility {
    PUBLIC {
        @Override
        public String toString() {
            return "public";
        }
    },

    PRIVATE {
        @Override
        public String toString() {
            return "private";
        }
    };
}

package ir.translation;

public enum  Status
{
    SUCCESS,
    UNSUPPORTED {
        @Override
        public String toString() {
            return "Unsupported";
        }
    }
}

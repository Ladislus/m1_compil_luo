package ir.com;

class LabelString extends Label{
    private String label;

    @Override
    public String toString() {
        return label;
    }

    public LabelString(String label) {
        this.label = label;
    }
}


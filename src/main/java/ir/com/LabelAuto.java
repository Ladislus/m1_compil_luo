package ir.com;

class LabelAuto extends Label{
    private int label;
    private static int lastLabel;

    @Override
    public String toString() {
        return "L" + label;
    }

    public LabelAuto() {
        this.label = lastLabel;
        lastLabel += 1;
    }

}


package ir.com;

public abstract class Label extends Command{

    public static Label fresh(){
        return new LabelAuto();
    }

    public static Label named(String name){
        return new LabelString(name);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

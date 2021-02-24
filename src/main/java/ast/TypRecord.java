package ast;

import java.util.ArrayList;
import java.util.List;

public class TypRecord extends Type {
    private String name;
    private List<Type> attributeTypes;

    public TypRecord(String name, List<Type> attributeTypes) {
        this.name = name;
        this.attributeTypes = new ArrayList<>(attributeTypes);
    }

    public String getName() {
        return name;
    }

    public List<Type> getAttributeTypes() {
        return attributeTypes;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

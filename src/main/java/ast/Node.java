package ast;

import java.util.Objects;

/*@ All the classes that constitute the nodes of the
    abstract syntax trees of the LUO language should
    inherit from the interface Node.
    This is necessary to implement an ANTLR Visitor
    for the parse tree to generate the corresponding
    abstract syntax tree.
 */
public abstract class Node {
    protected Position position;

    public Position getPosition() {
        return position;
    }

    public abstract <T> T accept(Visitor<T> visitor);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return position.equals(node.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}

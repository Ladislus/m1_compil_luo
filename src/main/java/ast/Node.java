package ast;

/*@ All the classes that constitute the nodes of the
    abstract syntax trees of the LUO language should
    inherit from the interface Node.
    This is necessary to implement an ANTLR Visitor
    for the parse tree to generate the corresponding
    abstract syntax tree.
 */
public interface Node {
    <T> T accept(Visitor<T> visitor);
}

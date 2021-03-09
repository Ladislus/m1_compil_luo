package ast;

public class Position {
  private int line;
  private int column;

  public Position(int line, int column) {
    this.line = line;
    this.column = column;
  }

  @Override
  public String toString() {
    return "[line=" + line + ", column=" + column + "]";
  }
}
package semantic_analysis;

import ast.VisitorDefault;

import java.util.Optional;

public class TypeChecker
  extends VisitorDefault<Optional<ast.Type>> //ToDo: should be changed to VisitorBase when finished
{
  private SymbolTable symbolTable;
  private VisitedBlocks visitedBlocks;

  public VisitedBlocks getVisitedBlocks() {
    return visitedBlocks;
  }

  public TypeChecker(SymbolTable symbolTable) {
    super(Optional.empty());
    this.symbolTable = symbolTable;
    this.visitedBlocks = new VisitedBlocks();
  }
}

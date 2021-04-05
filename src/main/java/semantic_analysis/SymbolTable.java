package semantic_analysis;

import ast.InsBlock;
import ast.Type;
import support.ListTools;
import support.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SymbolTable {
  // functions is the symbol table for LUO function definitions.
  // As LUO supports overloading, a function name may be associated
  // with several different signatures.
  private final Map<String, List<Signature>> functions;
  // blocks is a map that associates each block with its local table.
  private final Map<InsBlock, Map<String, Type>> blocks;
  // globals is a map that associates each global variable with its type.
  private final Map<String, Type> globalVariables;
  // userTypes is a map that associates each user-defined type name with
  // its definition. As we will use the association is both direction,
  // this map is implemented as a list of pairs.
  private final List<Pair<String, List<Pair<String, Type>>>> userTypes;

  protected SymbolTable() {
    functions = new HashMap<>();
    Signatures.premade.forEach((key, value) -> addFunction(key.toString(), value));

    blocks = new HashMap<>();
    globalVariables = new HashMap<>();
    userTypes = new ArrayList<>();
  }

  /**
   * Add a function's signature to the global symbol table.
   * This function doesn't check if this new signature makes
   * the overloading mechanism ambiguous.
   *
   * @param functionName The name of the user-defined function
   * @param signature    The signature of the user-defined function
   */
  public void addFunction(String functionName, Signature signature) {
    List<Signature> signatures = this.functions.get(functionName);
    if (signatures == null)
      signatures = new ArrayList<>();
    signatures.add(signature);
    this.functions.put(functionName, signatures);
  }

  /**
   * Add an association between a type name and a type definition.
   *
   * @param typeName       The name of the user-defined type
   * @param typeDefinition The definition of the user-defined record type
   */
  public void addTypeDefinition(String typeName, List<Pair<String, Type>> typeDefinition) {
    this.userTypes.add(new Pair<>(typeName, typeDefinition));
  }

  /**
   * Get the record type definition associated with a type name.
   *
   * @param typeName The name of the type looked for
   * @return Optional.empty() if the name is not associated with a definition.
   */
  public Optional<List<Pair<String, Type>>> typeDefinitionLookup(String typeName) {
    Optional<Pair<String, List<Pair<String, Type>>>> typeDefinition =
      this.userTypes.stream()
        .filter((Pair<String, ?> pairNameDefinition) -> pairNameDefinition.getFst().equals(typeName))
        .findFirst();
    return typeDefinition.map(Pair::getSnd);
  }

  /**
   * Get a type name and its definition from the lists of its field names.
   *
   * @param fieldNames The list of the names of the fields of a record type
   * @return A type name and definition, if it exists
   */
  public Optional<Pair<String, List<Pair<String, Type>>>> typeNameLookup(List<String> fieldNames) {
//      return this.userTypes.stream()
//              .filter((Pair<String, ?> pairNameDefinition) -> pairNameDefinition.getSnd().equals(fieldNames))
//              .findFirst();
      return this.userTypes.stream()
              .filter((Pair<String, List<Pair<String, Type>>> pairNameDefinition) -> ListTools.getFstList(pairNameDefinition.getSnd()).equals(fieldNames))
              .findFirst();
  }

  /*
  , Position pos, Errors errors){
    List<Signature> signatures = this.functions.get(functionName);
    if (signatures == null) {
      signatures = new ArrayList<>();
      signatures.add(signature);
      this.functions.put(functionName, signatures);
    } else {
      boolean signatureWithSameArgumentTypes =
        signatures
          .stream()
          .anyMatch((sig) -> signature.getArgumentsTypes().equals(sig.getArgumentsTypes()));
      if (signatureWithSameArgumentTypes)
        errors.add("At position " + pos + " the signature of the function "
          + functionName + " makes the overloading ambiguous.");
    }
  }
  */


  /**
   * Add a variable's type to the local table of the given block and
   * return the previous type if any.
   *
   * @param block    The block of instructions
   * @param variable The name of the local variable
   * @param type     The type of the local variable
   */
  public void addVariable(InsBlock block, String variable, Type type) {
    blocks.get(block).put(variable, type);
  }

  /**
   * Add a variable's type to the global table and
   * return the previous type if any.
   *
   * @param variable The name of the local variable
   * @param type     The type of the local variable
   */
  public void addGlobalVariable(String variable, Type type) {
    globalVariables.put(variable, type);
  }

  /**
   * If a local table already exists for the block, do nothing,
   * otherwise create a new empty local table.
   *
   * @param block The considered block of instructions
   */
  public void createLocalTable(InsBlock block) {
    if (blocks.get(block) == null) {
      Map<String, Type> localTable = new HashMap<>();
      blocks.put(block, localTable);
    }
  }

  /**
   * Return the list of signatures associated with the
   * function name.
   *
   * @param functionName The name of the function
   * @return a list of signatures
   */
  public List<Signature> functionLookup(String functionName) {
    List<Signature> signatures = functions.get(functionName);
    if (signatures != null)
      return signatures;
    return new ArrayList<>();
  }

  /**
   * Try to get the type associated with a variable name.
   * It looks first in the current block, then its parent, etc.,
   * and final in the global variables.
   *
   * @param variable      The variable to search for
   * @param visitedBlocks The stack of blocks currently visited
   * @return The type if found. Optional.empty() indicates
   * the search was unsuccessful.
   */
  public Optional<Type> variableLookup(String variable, VisitedBlocks visitedBlocks) {
    for (InsBlock block : visitedBlocks.getStack()) {
      Map<String, Type> blockSymbolTable = blocks.get(block);
      assert (blockSymbolTable != null) :
        "[SymbolTable] No local table associated to the block: please report";
      Type type = blockSymbolTable.get(variable);
      if (type != null)
        return Optional.of(type);
    }
    Type type = globalVariables.get(variable);
    if (type != null)
      return Optional.of(type);
    return Optional.empty();
  }
}

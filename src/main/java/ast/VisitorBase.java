package ast;

import support.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class VisitorBase<T> implements Visitor<T> {

  // ##########################
  // #    Block Expression    #
  // ##########################

  /**
   * @author Ladislas WALCAK
   * @author Thomas QUETIER
   * @author Corentin HERVOCHON
   * @author Martin GUERRAUD
   */

  @Override
  public T visit(ExpUnaryOperation operation) {
    return operation.getExpression().accept(this);
  }

  @Override
  public T visit(ExpBinaryOperation operation) {
    operation.getLeft().accept(this);
    return operation.getRight().accept(this);
  }

  @Override
  public T visit(ExpBoolean bool) {
    return null;
  }

  @Override
  public T visit(ExpCharacter character) {
    return null;
  }

  @Override
  public T visit(ExpInteger integer) {
    return null;
  }

  @Override
  public T visit(ExpVariable variable) {
    return null;
  }

  @Override
  public T visit(ExpRecordAccess record) {
    return record.getRecord().accept(this);
  }

  @Override
  public T visit(ExpArrayAccess array) {
    array.getArray().accept(this);
    return array.getIndex().accept(this);
  }

  @Override
  public T visit(ExpArrayEnum enumeration) {
    T curr = null;
    for (Expression exp : enumeration.getElements())
      curr = exp.accept(this);
    return curr;
  }

  @Override
  public T visit(ExpRecordEnum enumeration) {
    List<Expression> expressionList =
      enumeration.getFieldValues()
        .stream()
        .map(Pair::getSnd)
        .collect(Collectors.toList());
    T curr = null;
    for (Expression exp : expressionList) curr = exp.accept(this);
    return curr;
  }

  @Override
  public T visit(ExpFunctionCall function) {
    T curr = null;
    for (Expression exp : function.getArguments())
      curr = exp.accept(this);
    return curr;
  }

  @Override
  public T visit(ExpString string) {
    return null;
  }

  @Override
  public T visit(ExpNew expression) {
    expression.getType().accept(this);
    return expression.getArgument().accept(this);
  }
  // ##############################
  // #    Fin Block Expression    #
  // ##############################


  /**
   * Expression de types
   *
   * @author GUINDO Mouctar Ousseini
   * @author GBOHO Thierry
   * @author HEBRAS Jerome
   * @author GUINGOUAIN Nicolas
   */

  @Override
  public T visit(TypPrimitive typPrimitive) {
    return null;
  }

  @Override
  public T visit(TypDico typDictionary) {
    return typDictionary.getType().accept(this);
  }

  @Override
  public T visit(TypVariable typRecord) {
    return null;
  }

  @Override
  public T visit(TypArray typArray) {
    return typArray.getType().accept(this);
  }

  // #################################################################################################
  // # Bloc Definition de fonction, définition de type, imports, déclarations globales et programmes #
  // #################################################################################################

  /* Authors
   * @author  Nicolas ZHOU
   * @author  Marion JURE
   * @author  Mathis QUERAULT
   * @author  Tristan LE SAUX
   */

  /**
   * Cette methode visite l'arbre d'une fonction de façon arbitraire :
   * Declaration -> Instruction
   *
   * @param function Un noeud Function
   * @return La valeur du dernier enfant visité.
   */
  @Override
  public T visit(Function function) {
    for (Declaration parameter : function.getParameters())
      parameter.accept(this);
    return function.getBody().accept(this);
  }

  @Override
  public T visit(TypeDefinition typeDefinition) {
    T curr = null;
    for (Declaration declaration : typeDefinition.getDeclarations())
      curr = declaration.accept(this);
    return curr;
  }

  @Override
  public T visit(Import imports) {
    return null;
  }

  @Override
  public T visit(GlobalDeclaration globalDeclaration) {
    T result = globalDeclaration.getType().accept(this);
    if (globalDeclaration.getExpression().isPresent())
      return globalDeclaration.getExpression().get().accept(this);
    else
      return result;
  }

  /**
   * Cette methode visite l'arbre du programme de façon arbitraire :
   * Imports -> GlobalDecelaration -> TypeDefinition -> Function
   *
   * @param program a Program node
   * @return La valeur du dernier enfant visité
   */
  @Override
  public T visit(Program program) {
    T curr = null;
    for (Import my_import : program.getImports())
      curr = my_import.accept(this);
    for (GlobalDeclaration globalDeclaration : program.getGlobalDeclarations())
      curr = globalDeclaration.accept(this);
    for (TypeDefinition typeDefinition : program.getTypeDefinitions())
      curr = typeDefinition.accept(this);
    for (Function function : program.getFunctions())
      curr = function.accept(this);
    return curr;
  }

  // #####################################################################################################
  // # FIN Bloc Definition de fonction, définition de type, imports, déclarations globales et programmes #
  // #####################################################################################################

  // ##################################
  // #    Block Declaration Locale    #
  // #################################

  /**
   * @author Letord Baptiste
   * @author Arnaud Pasquier
   * @author Habib Bayoussoula
   */

  public T visit(Declaration declaration) {
    if (declaration.getExpression().isPresent()) {
      declaration.getType().accept(this);
      return declaration.getExpression().get().accept(this);
    } else {
      return declaration.getType().accept(this);
    }
  }
  // ######################################
  // #    Fin Block Declaration Locale    #
  // ######################################

  // ##########################
  // #    Block Instruction   #
  // ##########################

  /**
   * @author BENAI Mahmoud
   * @author BONHOMME Hugo
   * @author BRAHIM AZIB Youssouf
   * @author VADET Alexandre
   */

  @Override
  public T visit(InsFor instruction) {
    instruction.getDeclaration().accept(this);
    instruction.getRange().accept(this);
    instruction.getStep().accept(this);
    return instruction.getBody().accept(this);

  }

  @Override
  public T visit(InstForeach instruction) {
    instruction.getDeclaration().accept(this);
    instruction.getCollection().accept(this);
    return instruction.getBody().accept(this);
  }

  @Override
  public T visit(InsWhile instruction) {
    if (instruction.getDoWhile()) {
      instruction.getBody().accept(this);
      return instruction.getCondition().accept(this);
    } else {
      instruction.getCondition().accept(this);
      return instruction.getBody().accept(this);
    }

  }

  @Override
  public T visit(InsIf instruction) {
    instruction.getCondition().accept(this);
    T curr = null;
    if (instruction.getElseif().isEmpty()) {
      if (!instruction.getBodyElse().isPresent()) {
        return instruction.getBody().accept(this);
      } else {
        instruction.getBody().accept(this);
        return instruction.getBodyElse().get().accept(this);
      }
    } else {
      instruction.getBody().accept(this);
      for (Pair<Expression, Instruction> pair : instruction.getElseif()) {
        pair.getFst().accept(this);
        curr = pair.getSnd().accept(this);
      }
      if (!instruction.getBodyElse().isPresent()) {
        return curr;
      } else {
        return instruction.getBodyElse().get().accept(this);
      }
    }
  }

  @Override
  public T visit(InsAssign instruction) {
    instruction.getlValue().accept(this);
    return instruction.getExpression().accept(this);
  }

  @Override
  public T visit(InsBlock instruction) {
    T curr = null;
    for (Declaration declaration : instruction.getDeclarations()) {
      curr = declaration.accept(this);
    }
    for (Instruction instruction1 : instruction.getBody()) {
      curr = instruction1.accept(this);
    }
    return curr;
  }

  @Override
  public T visit(InsBreak instruction) {
    return null;
  }

  @Override
  public T visit(InsExpression instruction) {
    return instruction.getExpression().accept(this);
  }

  @Override
  public T visit(InsReturn instruction) {
    return instruction.getExpression().accept(this);
  }

  // ##########################
  // #    Fin Instruction     #
  // ##########################

}

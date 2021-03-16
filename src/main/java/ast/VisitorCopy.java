package ast;

import support.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VisitorCopy implements Visitor<Node> {

  // ToDo: il faut ajouter le paramètre position à tous les constructeurs
  // ToDo: à corriger, beaucoup de cas ne font PAS de copie profonde.

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
  public Node visit(ExpUnaryOperation operation) {
    return new ExpUnaryOperation(operation.getPosition().copy(),
      operation.getOperator(),
      (Expression) operation.getExpression().accept(this),
      operation.isPrefix());
  }

  @Override
  public Node visit(ExpBinaryOperation operation) {
    new ExpBinaryOperation(operation.getPosition().copy(), (Expression) operation.getLeft().accept(this), operation.getOperator(), (Expression) operation.getRight().accept(this));
  }

  @Override
  public Node visit(ExpBoolean bool) {
    return new ExpBoolean(bool.getPosition().copy(), bool.getValue());
  }

  @Override
  public Node visit(ExpCharacter character) {
    return new ExpCharacter(character.getPosition().copy(), character.getValue());
  }

  @Override
  public Node visit(ExpInteger integer) {
    return new ExpInteger(integer.getPosition().copy(), integer.getValue());
  }

  @Override
  public Node visit(ExpVariable variable) {
    return new ExpVariable(variable.getPosition().copy(), new String(variable.getVariable()));
  }

  @Override
  public Node visit(ExpRecordAccess record) {
    return new ExpRecordAccess(record.getPosition().copy(), (Expression) record.getRecord().accept(this), new String(record.getField()));
  }

  @Override
  public Node visit(ExpArrayAccess array) {
    return new ExpArrayAccess(array.getPosition().copy(), (Expression) array.getArray().accept(this), (Expression) array.getIndex().accept(this));
  }

  @Override
  public Node visit(ExpArrayEnum enumeration) {
    List<Expression> ne = new ArrayList<>();
    for (Expression exp : enumeration.getElements()) ne.add((Expression) exp.accept(this));
    return new ExpArrayEnum(enumeration.getPosition().copy(), ne);
  }

  @Override
  public Node visit(ExpTuple tuple) {
    return new ExpTuple(tuple.getPosition().copy(), (Expression) tuple.getFirst().accept(this), (Expression) tuple.getSecond().accept(this));
  }

  @Override
  public Node visit(ExpFunctionCall function) {
    List<Expression> na = new ArrayList<>();
    for (Expression exp : function.getArguments()) na.add((Expression) exp.accept(this));
    return new ExpFunctionCall(function.getPosition().copy(), new String(function.getName()), na);
  }

  @Override
  public Node visit(ExpString string) {
    return new ExpString(string.getPosition().copy(), new String(string.getValue()));
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
  public Node visit(TypPrimitive typPrimitive) {
    return new TypPrimitive(typPrimitive.getPosition().copy(), typPrimitive.getType());
  }

  @Override
  public Node visit(TypDico typDictionary) {
    Type type = (Type)typDictionary.getType().accept(this);
    return new TypDico(typDictionary.getPosition().copy(), type);
  }

  @Override
  public Node visit(TypVariable typRecord) {
    return new TypPrimitive(typPrimitive.getPosition().copy(), new String(typRecord.getName()));
  }

  @Override
  public Node visit(TypArray typArray) {
    Type type = (Type)typArray.getType().accept(this);
    return new TypArray(typPrimitive.getPosition().copy(), type);
  }

  // #################################################################################################
  // # Bloc Definition de fonction, définition de type, imports, déclarations globales et programmes #
  // #################################################################################################

  /**
   * @author Nicolas ZHOU
   * @author Marion JURE
   * @author Mathis QUERAULT
   * @author Tristan LE SAUX
   */

  @Override
  public Node visit(Function function) {
    // ToDo: beaucoup de changements à faire à cause de la correction de Function
    List<Declaration> declarations = new ArrayList<>();
    for (Declaration declaration : function.getParameters()) {
      declarations.add((Declaration) declaration.accept(this));
    }

    List<Instruction> instructions = new ArrayList<>();
    for (Instruction instruction : function.getInstructions()) {
      instructions.add((Instruction) instruction.accept(this));
    }

    Type type = (Type) function.getReturn_type().accept(this);

    return new Function(instructions, declarations, type, function.getVisibility());
  }

  @Override
  public Node visit(TypeDefinition typeDefinition) {
    List<Declaration> declarations = new ArrayList<>();
    for (Declaration declaration : typeDefinition.getDeclarations()) {
      declarations.add((Declaration) declaration.accept(this));
    }
    return new TypeDefinition(typeDefinition.position.copy(), new String(typeDefinition.getName()), declarations);
  }

  @Override
  public Node visit(Import imports) {
    return new Import(imports.position.copy(), new String(imports.getPath()));
  }

  @Override
  public Node visit(GlobalDeclaration globalDeclaration) {
    Expression expression = (Expression) globalDeclaration.getExpression().get().accept(this);
    Type type = (Type) globalDeclaration.getType().accept(this);
    return new GlobalDeclaration(globalDeclaration.position.copy(),
      globalDeclaration.getVisibility(), type,
      new String(globalDeclaration.getVariable()), expression);
  }

  @Override
  public Node visit(Program program) {
    List<Import> importList = new ArrayList<>();
    ;
    for (Import my_import : program.getImports()) {
      importList.add((Import) my_import.accept(this));
    }

    List<GlobalDeclaration> globalDeclarationList = new ArrayList<>();
    ;
    for (GlobalDeclaration globalDeclaration : program.getGlobalDeclarations()) {
      globalDeclarationList.add((GlobalDeclaration) globalDeclaration.accept(this));
    }

    List<TypeDefinition> typeDefinitionList = new ArrayList<>();
    for (TypeDefinition typeDefinition : program.getTypeDefinitions()) {
      typeDefinitionList.add((TypeDefinition) typeDefinition.accept(this));
    }
    List<Function> functionList = new ArrayList<>();
    for (Function function : program.getFunctions()) {
      functionList.add((Function) function.accept(this));
    }

    return new Program(program.position.copy(), importList, globalDeclarationList, typeDefinitionList, functionList);
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

  @Override
  public Node visit(Declaration declaration){
    Type type = (Type) declaration.getType().accept(this);
    if (declaration.getExpression().isEmpty()){
      return new Declaration(declaration.getPosition().copy(), type, declaration.getVariable());
    }
    else {
      return new Declaration(declaration.getPosition().copy(),
              type,
              declaration.getVariable(),
              (Expression) declaration.getExpression().get().accept(this));
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
  public Node visit(InsFor instruction) {
    return new InsFor(
            instruction.getPosition().copy(),
            (Declaration) instruction.getDeclaration().accept(this),
            (Expression) instruction.getRange().accept(this),
            (Expression) instruction.getStep().accept(this),
            (Instruction) instruction.getBody().accept(this)
    );
  }

  @Override
  public Node visit(InsWhile instruction) {
    if(instruction.getDoWhile()){
      InsWhile inst_do_while = InsWhile.insDoWhile(instruction.getPosition().copy(),
              (Expression) instruction.getCondition().accept(this),
              (Instruction) instruction.getBody().accept(this));
      return inst_do_while;
    }
    else {
      InsWhile inst_while = InsWhile.insWhile(instruction.getPosition().copy(),
              (Expression) instruction.getCondition().accept(this),
              (Instruction) instruction.getBody().accept(this));
      return inst_while;
    }
  }

  @Override
  public Node visit(InstForeach instruction) {
    return new InstForeach(
            instruction.getPosition().copy(),
            (Type) instruction.getType().accept(this),
            instruction.getIdentifier(),
            (Expression) instruction.getCollection().accept(this),
            (Instruction) instruction.getBody().accept(this)
    );
  }

  @Override
  public Node visit(InsIf instruction) {
    List<Pair> pairList = new ArrayList<>();
    for(Pair pair : instruction.getElseif()){
      Pair<Expression, Instruction> newPair = new Pair<>((Expression) pair.getFst().accept(this),(Instruction) pair.getSnd().accept(this));
      pairList.add(newPair);
    }

    return new InsIf(
            instruction.getPosition().copy(),
            (Expression) instruction.getCondition().accept(this),
            (Instruction) instruction.getBody().accept(this),
            pairList,
            (Optional<Instruction>) instruction.getBodyElse().get().accept(this)
    );
  }

  @Override
  public Node visit(InsAssign instruction) {
    return new InsAssign(
            instruction.getPosition().copy(),
            (Expression) instruction.getlValue().accept(this),
            (EnumAssignOp) instruction.getOperation(),
            (Expression) instruction.getExpression().accept(this)
    );
  }

  @Override
  public Node visit(InsBlock instruction) {
    List<Declaration> declarationList = new ArrayList<>();
    for(Declaration declaration : instruction.getDeclarations()){
      declarationList.add((Declaration) declaration.accept(this));
    }

    List<Instruction>expressionList = new ArrayList<>();
    for(Instruction instruction1 : instruction.getBody()){
      expressionList.add((Instruction) instruction1.accept(this));
    }

    return new InsBlock(
            instruction.getPosition().copy(),
            declarationList,
            expressionList
    );
  }

  @Override
  public Node visit(InsBreak instruction) {
    return new InsBreak(
            instruction.getPosition().copy()
    );
  }

  @Override
  public Node visit(InsExpression instruction) {
    return new InsExpression(
            instruction.getPosition().copy(),
            (Expression) instruction.getExpression().accept(this)
    );
  }

  @Override
  public Node visit(InsReturn instruction) {
    return new InsReturn(
            instruction.getPosition().copy(),
            (Expression) instruction.getExpression().accept(this)
    );
  }

  // ##########################
  // #    Fin Instruction     #
  // ##########################
}

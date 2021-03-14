package ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import parser.LuoBaseVisitor;
import parser.LuoLexer;
import parser.LuoParser;
import parser.LuoParser.ProgramContext;
import support.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Build extends LuoBaseVisitor<Node> {

  // Building an AST Position from an ANTLR Context
  private static Position position(ParserRuleContext ctx) {
    return new Position(ctx.start.getLine(),
      ctx.start.getCharPositionInLine());
  }

  private <T> List<T> makeList(List<? extends ParserRuleContext> contexts) {
    List<T> nodes = new ArrayList<>();
    for (ParserRuleContext context : contexts)
      nodes.add((T) context.accept(this));
    return nodes;
  }

  private EnumVisibility getVisibility(Token token) {
    if (token != null && token.getType() == LuoLexer.Private)
        return EnumVisibility.PRIVATE;
    return EnumVisibility.PUBLIC;
  }

  @Override
  public Node visitProgram(ProgramContext ctx) {
    List<Import> imports = makeList(ctx.imports());
    List<GlobalDeclaration> globalDeclarations = makeList(ctx.global_declaration());
    List<TypeDefinition> typeDefinitions = makeList(ctx.type_definition());
    List<Function> functions = makeList(ctx.function_definition());
    return new Program(position(ctx), imports, globalDeclarations, typeDefinitions, functions);
  }

  @Override
  public Node visitGlobal_declaration(LuoParser.Global_declarationContext ctx) {
    Declaration declaration = (Declaration) ctx.declaration().accept(this);
    Type type = declaration.getType();
    String variable = declaration.getVariable();
    Position position = position(ctx);
    Optional<Expression> optionalExpression = declaration.getExpression();
    EnumVisibility visibility = getVisibility(ctx.Visibility().getSymbol());
    return new GlobalDeclaration(position, visibility, type, variable, optionalExpression);
  }

  @Override
  public Node visitLocalDeclarationInit(LuoParser.LocalDeclarationInitContext ctx) {
    Type type = (Type) ctx.type_expression().accept(this);
    String variable = ctx.Identifier().getText();
    Expression expression = (Expression) ctx.expression().accept(this);
    return new Declaration(position(ctx), type, variable, expression);
  }

  @Override
  public Node visitLocalDeclaration(LuoParser.LocalDeclarationContext ctx) {
    Type type = (Type) ctx.type_expression().accept(this);
    String variable = ctx.Identifier().getText();
    return new Declaration(position(ctx), type, variable);
  }

  @Override
  public Node visitBlock(LuoParser.BlockContext ctx) {
    List<Declaration> declarations = makeList(ctx.declaration());
    List<Instruction> instructions = makeList(ctx.instruction());
    return new InsBlock(position(ctx), declarations, instructions);
  }

  @Override
  public Node visitInsIf(LuoParser.InsIfContext ctx) {
    List<Expression> expressions = makeList(ctx.expression());
    List<Instruction> instructions = makeList(ctx.expression());

    int countElseif = ctx.Elseif().size();
    int countElse = ctx.Else() == null ? 0 : 1;
    assert(expressions.size() == 1 + countElseif + countElse);

    Expression condition = expressions.get(0);
    Instruction thenBranch = instructions.get(0);
    List<Pair<Expression, Instruction>> elseIfList = new ArrayList<>();
    for(int counter = 0; counter < countElseif; counter++)
      elseIfList.add(new Pair<>(expressions.get(counter+1), instructions.get(counter+1)));

    if (countElse == 1){
      Instruction elseBranch = instructions.get(instructions.size()-1);
      return new InsIf(position(ctx), condition, thenBranch, elseIfList, elseBranch);
    }
    return new InsIf(position(ctx), condition, thenBranch, elseIfList);
  }

  @Override
  public Node visitInsForeach(LuoParser.InsForeachContext ctx) {
    Type type = (Type) ctx.type_expression().accept(this);
    String variable = ctx.Identifier().getText();
    Expression collection = (Expression) ctx.expression().accept(this);
    Instruction body = (Instruction) ctx.instruction().accept(this);
    return new InstForeach(position(ctx), type, variable, collection, body);
  }

  @Override
  public Node visitInsFor(LuoParser.InsForContext ctx) {
    Declaration declaration = (Declaration) ctx.declaration().accept(this);
    Expression condition = (Expression) ctx.expression(0).accept(this);
    Expression step = (Expression) ctx.expression(1).accept(this);
    Instruction body = (Instruction) ctx.instruction().accept(this);
    return new InsFor(position(ctx), declaration, condition, step, body);
  }

  @Override
  public Node visitInsWhile(LuoParser.InsWhileContext ctx) {
    Expression condition = (Expression) ctx.expression().accept(this);
    Instruction body = (Instruction) ctx.instruction().accept(this);
    return InsWhile.insWhile(position(ctx), condition, body);
  }

  @Override
  public Node visitInsDowhile(LuoParser.InsDowhileContext ctx) {
    Expression condition = (Expression) ctx.expression().accept(this);
    Instruction body = (Instruction) ctx.instruction().accept(this);
    return InsWhile.insDoWhile(position(ctx), condition, body);
  }

  @Override
  public Node visitInsExpression(LuoParser.InsExpressionContext ctx) {
    Expression expression = (Expression) ctx.expression().accept(this);
    return new InsExpression(position(ctx), expression);
  }

  @Override
  public Node visitInsBlock(LuoParser.InsBlockContext ctx) {
    List<Declaration> declarations = makeList(ctx.block().declaration());
    List<Instruction> instructions = makeList(ctx.block().instruction());
    return new InsBlock(position(ctx), declarations, instructions);
  }

  @Override
  public Node visitInsReturn(LuoParser.InsReturnContext ctx) {
    Expression expression = (Expression) ctx.expression().accept(this);
    return new InsReturn(position(ctx), expression);
  }

  @Override
  public Node visitInsBreak(LuoParser.InsBreakContext ctx) {
    return new InsBreak(position(ctx));
  }

  private EnumAssignOp getAssignOp(Token token){
    switch(token.getType()){
      case LuoLexer.PlusEqual: return EnumAssignOp.PLUSEQUAL;
      case LuoLexer.MinusEqual: return EnumAssignOp.MINEQUAL;
      case LuoLexer.MultEqual: return EnumAssignOp.MULTEQUAL;
      case LuoLexer.DivEqual: return EnumAssignOp.DIVEQUAL;
    }
    return EnumAssignOp.EQUAL;
  }

  @Override
  public Node visitInsAssign(LuoParser.InsAssignContext ctx) {
    Expression lValue = (Expression) ctx.expression(0).accept(this);
    Expression expression = (Expression) ctx.expression(1).accept(this);
    return new InsAssign(position(ctx), lValue, getAssignOp(ctx.op), expression);
  }

  private EnumBinaryOp getBinaryOp(Token token){
    switch(token.getType()){
      case LuoLexer.LogicalOr: return EnumBinaryOp.OR;
      case LuoLexer.LogicalAnd: return EnumBinaryOp.AND;
      case LuoLexer.Plus: return EnumBinaryOp.ADD;
      case LuoLexer.Minus: return EnumBinaryOp.SUB;
      case LuoLexer.Multiplication: return EnumBinaryOp.MUL;
      case LuoLexer.Division: return EnumBinaryOp.DIV;
      case LuoLexer.Modulo: return EnumBinaryOp.MOD;
      case LuoLexer.Different: return EnumBinaryOp.DIFF;
      case LuoLexer.LesserThan: return EnumBinaryOp.LT;
      case LuoLexer.LesserOrEqual: return EnumBinaryOp.LTE;
      case LuoLexer.GreaterThan: return EnumBinaryOp.GT;
      case LuoLexer.GreaterOrEqual: return EnumBinaryOp.GTE;
    }
    return EnumBinaryOp.EQ;
  }

  @Override
  public Node visitExpOr(LuoParser.ExpOrContext ctx) {
    Expression left = (Expression) ctx.expression(0).accept(this);
    Expression right = (Expression) ctx.expression(1).accept(this);
    return new ExpBinaryOperation(position(ctx), left, getBinaryOp(ctx.op), right);
  }

  @Override
  public Node visitExpMulDivMod(LuoParser.ExpMulDivModContext ctx) {
    Expression left = (Expression) ctx.expression(0).accept(this);
    Expression right = (Expression) ctx.expression(1).accept(this);
    return new ExpBinaryOperation(position(ctx), left, getBinaryOp(ctx.op), right);
  }

  @Override
  public Node visitExpComparison(LuoParser.ExpComparisonContext ctx) {
    Expression left = (Expression) ctx.expression(0).accept(this);
    Expression right = (Expression) ctx.expression(1).accept(this);
    return new ExpBinaryOperation(position(ctx), left, getBinaryOp(ctx.op), right);
  }

  @Override
  public Node visitExpCharacter(LuoParser.ExpCharacterContext ctx) {
    return super.visitExpCharacter(ctx);
  }

  @Override
  public Node visitExpFunctionCall(LuoParser.ExpFunctionCallContext ctx) {
    return super.visitExpFunctionCall(ctx);
  }

  @Override
  public Node visitExpPreUnary(LuoParser.ExpPreUnaryContext ctx) {
    return super.visitExpPreUnary(ctx);
  }

  @Override
  public Node visitExpOpposite(LuoParser.ExpOppositeContext ctx) {
    return super.visitExpOpposite(ctx);
  }

  @Override
  public Node visitExpPostUnary(LuoParser.ExpPostUnaryContext ctx) {
    return super.visitExpPostUnary(ctx);
  }

  @Override
  public Node visitExpAddSub(LuoParser.ExpAddSubContext ctx) {
    Expression left = (Expression) ctx.expression(0).accept(this);
    Expression right = (Expression) ctx.expression(1).accept(this);
    return new ExpBinaryOperation(position(ctx), left, getBinaryOp(ctx.op), right);
  }

  @Override
  public Node visitExpInteger(LuoParser.ExpIntegerContext ctx) {
    return super.visitExpInteger(ctx);
  }

  @Override
  public Node visitExpAccessTabDico(LuoParser.ExpAccessTabDicoContext ctx) {
    return super.visitExpAccessTabDico(ctx);
  }

  @Override
  public Node visitExpParenthesis(LuoParser.ExpParenthesisContext ctx) {
    return ctx.expression().accept(this);
  }

  @Override
  public Node visitExpAnd(LuoParser.ExpAndContext ctx) {
    Expression left = (Expression) ctx.expression(0).accept(this);
    Expression right = (Expression) ctx.expression(1).accept(this);
    return new ExpBinaryOperation(position(ctx), left, getBinaryOp(ctx.op), right);
  }

  @Override
  public Node visitExpAccessRec(LuoParser.ExpAccessRecContext ctx) {
    Expression expression = (Expression) ctx.expression().accept(this);
    String field = ctx.Identifier().getText();
    return new ExpRecordAccess(position(ctx), expression, field);
  }

  @Override
  public Node visitExpIdentifier(LuoParser.ExpIdentifierContext ctx) {
    return new ExpVariable(position(ctx), ctx.Identifier().getText());
  }

  @Override
  public Node visitExpNot(LuoParser.ExpNotContext ctx) {
    return super.visitExpNot(ctx);
  }

  @Override
  public Node visitExpBoolean(LuoParser.ExpBooleanContext ctx) {

    return super.visitExpBoolean(ctx);
  }

  @Override
  public Node visitExpString(LuoParser.ExpStringContext ctx) {
    return super.visitExpString(ctx);
  }

  @Override
  public Node visitActual_parameter_list(LuoParser.Actual_parameter_listContext ctx) {
    return super.visitActual_parameter_list(ctx);
  }

  @Override
  public Node visitType_definition(LuoParser.Type_definitionContext ctx) {
    return super.visitType_definition(ctx);
  }

  @Override
  public Node visitTypArray(LuoParser.TypArrayContext ctx) {
    return super.visitTypArray(ctx);
  }

  @Override
  public Node visitTypPrimitive(LuoParser.TypPrimitiveContext ctx) {
    return super.visitTypPrimitive(ctx);
  }

  @Override
  public Node visitTypMap(LuoParser.TypMapContext ctx) {
    return super.visitTypMap(ctx);
  }

  @Override
  public Node visitTypIdentifier(LuoParser.TypIdentifierContext ctx) {
    return super.visitTypIdentifier(ctx);
  }

  @Override
  public Node visitDefinitionFunction(LuoParser.DefinitionFunctionContext ctx) {
    EnumVisibility visibility = getVisibility(ctx.Visibility().getSymbol());
    Type returnType = (Type) ctx.type_expression().accept(this);
    String name = ctx.Identifier().getText();
    List<Declaration> arguments = makeArgumentList(ctx.argument_list());
    InsBlock body = (InsBlock) ctx.block().accept(this);
    return new Function(position(ctx), visibility, name, arguments, body, Optional.of(returnType));
  }

  @Override
  public Node visitDefinitionProcedure(LuoParser.DefinitionProcedureContext ctx) {
    EnumVisibility visibility = getVisibility(ctx.Visibility().getSymbol());
    String name = ctx.Identifier().getText();
    List<Declaration> arguments = makeArgumentList(ctx.argument_list());
    InsBlock body = (InsBlock) ctx.block().accept(this);
    return new Function(position(ctx), visibility, name, arguments, body, Optional.empty());
  }

  private Declaration makeArgument(LuoParser.ArgumentContext ctx) {
    Type type = (Type) ctx.type_expression().accept(this);
    String variable = ctx.Identifier().getText();
    Optional<Expression> expression =
      ctx.expression() == null ? Optional.empty() :
        Optional.of((Expression) ctx.expression().accept(this));
    return new Declaration(position(ctx), type, variable, expression);
  }


  private List<Declaration> makeArgumentList(LuoParser.Argument_listContext ctx) {
    List<Declaration> arguments = new ArrayList<>();
    for(LuoParser.ArgumentContext argCtx : ctx.argument())
      arguments.add(makeArgument(argCtx));
    return arguments;
  }

  @Override
  public Node visitImports(LuoParser.ImportsContext ctx) {
    String path = ctx.Path().getText();
    return new Import(position(ctx), path);
  }
}

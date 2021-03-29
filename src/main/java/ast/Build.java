package ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.LuoBaseVisitor;
import parser.LuoLexer;
import parser.LuoParser;
import parser.LuoParser.ProgramContext;
import support.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Build extends LuoBaseVisitor<Node> {

  // Building an AST Position from an ANTLR Context
  private static Position position(ParserRuleContext ctx) {
    return new Position(ctx.start.getLine(),
            ctx.start.getCharPositionInLine());
  }

  @SuppressWarnings("unchecked")
  private <T,TC> List<T> makeList(List<? extends ParserRuleContext> contexts) {
    List<T> nodes = new ArrayList<>();
    for (ParserRuleContext context : contexts)
      nodes.add((T) context.accept(this));
    return nodes;
  }

  @Override
  public Node visitExpNew(LuoParser.ExpNewContext ctx) {
    Type type = (Type) ctx.type_expression().accept(this);
    Expression expression = (Expression) ctx.expression().accept(this);
    return new ExpNew(position(ctx), type, expression);
  }

  private EnumVisibility getVisibility(TerminalNode node) {
    if (node != null && node.getSymbol().getType() == LuoLexer.Private)
      return EnumVisibility.PRIVATE;
    return EnumVisibility.PUBLIC;
  }

  @Override
  public Node visitExpArrayEnumeration(LuoParser.ExpArrayEnumerationContext ctx) {
    List<Expression> expressions = makeList(ctx.expression_list().expression());
    return new ExpArrayEnum(position(ctx), expressions);
  }

  @Override
  public Node visitExpRecordEnumeration(LuoParser.ExpRecordEnumerationContext ctx) {
    List<Pair<String, Expression>> record = new ArrayList<>();
    int length = ctx.labeled_expression_list().expression().size();
    for(int counter = 0; counter < length; counter ++){
      String field = ctx.labeled_expression_list().Identifier(counter).getText();
      Expression expression = (Expression) ctx.labeled_expression_list().expression(counter).accept(this);
      record.add(new Pair<>(field, expression));
    }
    return new ExpRecordEnum(position(ctx), record);
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
    EnumVisibility visibility = getVisibility(ctx.Visibility());
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
    List<Instruction> instructions = makeList(ctx.instruction());

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

  private String escaping(String input){
    return input.replace("\\n", "\n")
            .replace("\\t", "\t")
            .replace("\\0", "\0")
            .replace("\\\"", "\"")
            .replace("\\'", "'")
            .replace("\\\\", "\\");
  }

  // The char constants are represented in the ParseTree by
  // a string. For example, if the NAP program contains '\n'
  // the ParseTree will contain a ECharContext node with text "'\\n'"
  // We have only a few escaped characters in nap.g4:
  // \n, \t, \\, \', \", \0
  @Override
  public Node visitExpCharacter(LuoParser.ExpCharacterContext ctx) {
    char character = escaping(ctx.getText()).charAt(1);
    return new ExpCharacter(position(ctx), character);
  }

  @Override
  public Node visitExpFunctionCall(LuoParser.ExpFunctionCallContext ctx) {
    String name = ctx.Identifier().getText();
    List<Expression> arguments = makeList(ctx.expression_list().expression());
    return new ExpFunctionCall(position(ctx), name, arguments);
  }

  private EnumUnaryOp getUnaryOp(Token token){
    switch(token.getType()){
      case LuoLexer.Minus: return EnumUnaryOp.MINUS;
      case LuoLexer.Negation: return EnumUnaryOp.NOT;
      case LuoLexer.PlusPlus:Plus: return EnumUnaryOp.INC;
      case LuoLexer.MinusMinus: return EnumUnaryOp.DEC;
    }
    throw new Error("ast.Build: Undefined unary operation");
  }

  @Override
  public Node visitExpPreUnary(LuoParser.ExpPreUnaryContext ctx) {
    Expression expression = (Expression) ctx.expression().accept(this);
    return new ExpUnaryOperation(position(ctx), getUnaryOp(ctx.op), expression);
  }

  @Override
  public Node visitExpOpposite(LuoParser.ExpOppositeContext ctx) {
    Expression expression = (Expression) ctx.expression().accept(this);
    return new ExpUnaryOperation(position(ctx), EnumUnaryOp.MINUS, expression);
  }

  @Override
  public Node visitExpPostUnary(LuoParser.ExpPostUnaryContext ctx) {
    Expression expression = (Expression) ctx.expression().accept(this);
    return new ExpUnaryOperation(position(ctx), expression, getUnaryOp(ctx.op));
  }

  @Override
  public Node visitExpAddSub(LuoParser.ExpAddSubContext ctx) {
    Expression left = (Expression) ctx.expression(0).accept(this);
    Expression right = (Expression) ctx.expression(1).accept(this);
    return new ExpBinaryOperation(position(ctx), left, getBinaryOp(ctx.op), right);
  }

  @Override
  public Node visitExpInteger(LuoParser.ExpIntegerContext ctx) {
    int value = Integer.parseInt(ctx.Integer().getText());
    return new ExpInteger(position(ctx), value);
  }

  @Override
  public Node visitExpAccessTabDico(LuoParser.ExpAccessTabDicoContext ctx) {
    Expression collection = (Expression) ctx.expression(0).accept(this);
    Expression index = (Expression) ctx.expression(1).accept(this);
    return new ExpArrayAccess(position(ctx), collection, index);
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
    Expression expression = (Expression) ctx.expression().accept(this);
    return new ExpUnaryOperation(position(ctx), EnumUnaryOp.NOT, expression);
  }

  @Override
  public Node visitExpBoolean(LuoParser.ExpBooleanContext ctx) {
    return new ExpBoolean(position(ctx), ctx.Boolean().getSymbol().equals("true"));
  }

  @Override
  public Node visitExpString(LuoParser.ExpStringContext ctx) {
    String escaped = escaping(ctx.getText());
    return new ExpString(position(ctx), escaped.substring(1, escaped.length()-1));
  }

  @Override
  public Node visitType_definition(LuoParser.Type_definitionContext ctx) {
    String name = ctx.Identifier(0).getText();
    List<String> fieldNames =
            ctx.Identifier()
                    .subList(1, ctx.Identifier().size())
                    .stream()
                    .map((t)->t.getText())
                    .collect(Collectors.toList());
    List<Type> fieldTypes =
            ctx.type_expression()
                    .stream()
                    .map((te)->(Type)te.accept(this))
                    .collect(Collectors.toList());
    assert fieldTypes.size() == fieldNames.size() : "ast.Build: type definition, field and type lists mismatch";
    List<Declaration> fields = new ArrayList<>();
    for(int counter = 0; counter < fieldNames.size(); counter++)
      fields.add(new Declaration(position(ctx.type_expression(counter)), fieldTypes.get(counter), fieldNames.get(counter)));
    return new TypeDefinition(position(ctx), name, fields);
  }

  @Override
  public Node visitTypArray(LuoParser.TypArrayContext ctx) {
    Type type = (Type) ctx.type_expression().accept(this);
    return new TypArray(position(ctx), type);
  }

  private EnumPrimitiveType getPrimitiveType(Token token){
    switch(token.getText()){
      case "int" : return EnumPrimitiveType.INT;
      case "char" : return  EnumPrimitiveType.CHAR;
      case "bool" : return  EnumPrimitiveType.BOOL;
    }
    throw new Error("ast.Build: unknown primitive type");
  }

  @Override
  public Node visitTypPrimitive(LuoParser.TypPrimitiveContext ctx) {
    return new TypPrimitive(position(ctx), getPrimitiveType(ctx.type));
  }

  @Override
  public Node visitTypMap(LuoParser.TypMapContext ctx) {
    Position position = position(ctx);
    Type type = new TypVariable(position, ctx.Identifier().getText());
    return new TypDico(position(ctx), type);
  }

  @Override
  public Node visitTypIdentifier(LuoParser.TypIdentifierContext ctx) {
    return new TypVariable(position(ctx), ctx.Identifier().getText());
  }

  @Override
  public Node visitDefinitionFunction(LuoParser.DefinitionFunctionContext ctx) {
    EnumVisibility visibility = getVisibility(ctx.Visibility());
    Type returnType = (Type) ctx.type_expression().accept(this);
    String name = ctx.Identifier().getText();
    List<Declaration> arguments =
      ctx.argument_list() == null ? new ArrayList<>() : makeList(ctx.argument_list().argument());
    InsBlock body = (InsBlock) ctx.block().accept(this);
    return new Function(position(ctx), visibility, name, arguments, body, Optional.of(returnType));
  }

  @Override
  public Node visitArgument(LuoParser.ArgumentContext ctx) {
    return makeArgument(ctx);
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
    return ctx.argument().stream()
            .map(this::makeArgument)
            .collect(Collectors.toList());
  }

  @Override
  public Node visitImports(LuoParser.ImportsContext ctx) {
    String path = ctx.String().getText();
    return new Import(position(ctx), path);
  }

  @Override
  public Node visitExpression_list(LuoParser.Expression_listContext ctx) {
    // Should not be called in this visitor
    throw new Error ("ast.Build: expression_list should not be visited.");
  }

  @Override
  public Node visitLabeled_expression_list(LuoParser.Labeled_expression_listContext ctx) {
    // Should not be called in this visitor
    throw new Error ("ast.Build: Labeled_expression_list should not be visited.");
  }

  @Override
  public Node visitArgument_list(LuoParser.Argument_listContext ctx) {
    // Should not be called in this visitor
    throw new Error ("ast.Build: argument_list should not be visited.");
  }
}

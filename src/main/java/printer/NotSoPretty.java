package printer;

import ast.*;
import support.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static support.ListTools.*;

public class NotSoPretty {

  public static List<String> toListString(Program program){
    NotSoPrettyVisitor visitor = new NotSoPrettyVisitor();
    return program.accept(visitor);
  }

  public static void print(Program program){
    for(String line : toListString(program))
      System.out.println(line);
  }

  static private class NotSoPrettyVisitor implements Visitor<List<String>> {

    private static String spaceAround(String s) {
      return " " + s + " ";
    }

    private static String parenthesis(String s) {
      return "(" + s + ")";
    }

    @Override
    public List<String> visit(InsReturn instruction) {
      String expression = stringFlatten(instruction.getExpression().accept(this));
      return singleton("return " + expression);
    }

    @Override
    public List<String> visit(ExpNew expression) {
      String type = stringFlatten(expression.getType().accept(this));
      String argument = stringFlatten(expression.getArgument().accept(this));
      return singleton("new " + type + parenthesis(argument));
    }

    @Override
    public List<String> visit(ExpRecordEnum enumeration) {
      List<String> fieldValues =
        enumeration.getFieldValues()
          .stream()
          .map((pair)->pair.getFst() + spaceAround("=") + stringFlatten(pair.getSnd().accept(this)))
          .collect(Collectors.toList());
      return singleton("{ " + commaSeparatedList(fieldValues) + " }");
    }

    @Override
    public List<String> visit(Program program) {
      List<String> listing = new ArrayList<>();
      for (Import anImport : program.getImports())
        listing.addAll(anImport.accept(this));
      for (GlobalDeclaration globalDeclaration : program.getGlobalDeclarations())
        listing.addAll(globalDeclaration.accept(this));
      for (TypeDefinition typeDefinition : program.getTypeDefinitions())
        listing.addAll(typeDefinition.accept(this));
      for (Function function : program.getFunctions())
        listing.addAll(function.accept(this));
      return listing;
    }

    @Override
    public List<String> visit(Declaration declaration) {
      String typeString = stringFlatten(declaration.getType().accept(this));
      String variableString = declaration.getVariable();
      if (declaration.getExpression().isPresent()) {
        String expression = stringFlatten(declaration.getExpression().get().accept(this));
        return singleton(typeString + " " + variableString + spaceAround("=") + expression);
      } else
        return singleton(typeString + " " + variableString);
    }

    @Override
    public List<String> visit(ExpUnaryOperation operation) {
      String expression = stringFlatten(operation.getExpression().accept(this));
      String operator = operation.getOperator().toString();
      if (operation.isPrefix())
        return singleton(parenthesis(operator + expression));
      else
        return singleton(parenthesis(expression + operator));
    }

    @Override
    public List<String> visit(ExpBinaryOperation operation) {
      String left = stringFlatten(operation.getLeft().accept(this));
      String right = stringFlatten(operation.getRight().accept(this));
      String operator = operation.getOperator().toString();
      return singleton(parenthesis(left + spaceAround(operator) + right));
    }

    @Override
    public List<String> visit(ExpBoolean bool) {
      return singleton(bool.getValue() ? "true" : "false");
    }

    @Override
    public List<String> visit(ExpCharacter character) {
      return singleton("'"+ deEscaping(character.getValue()+"") + "'");
    }

    @Override
    public List<String> visit(ExpInteger integer) {
      return singleton(integer.getValue() + "");
    }

    @Override
    public List<String> visit(ExpVariable variable) {
      return singleton(variable.getVariable());
    }

    @Override
    public List<String> visit(ExpRecordAccess record) {
      String expression = stringFlatten(record.getRecord().accept(this));
      String field = record.getField();
      return singleton(expression + "." + field);
    }

    @Override
    public List<String> visit(ExpArrayAccess array) {
      String arrayString = stringFlatten(array.getArray().accept(this));
      String indexString = stringFlatten(array.getIndex().accept(this));
      return singleton(arrayString + "[" + indexString + "]");
    }

    private List<String> visitNodes(List<? extends Node> nodes) {
      return flatten(nodes.stream()
        .map((exp) -> exp.accept(this))
        .collect(Collectors.toList()));
    }


    private String visitFlattenNodes(List<? extends Node> expressions) {
      List<String> list = expressions.stream()
        .map((exp) -> stringFlatten(exp.accept(this)))
        .collect(Collectors.toList());
      return commaSeparatedList(list);
    }

    @Override
    public List<String> visit(ExpArrayEnum enumeration) {
      return singleton("{ " + visitFlattenNodes(enumeration.getElements()) + " }");
    }

    @Override
    public List<String> visit(ExpFunctionCall function) {
      return singleton(function.getName() + parenthesis(visitFlattenNodes(function.getArguments())));
    }

    private static String deEscaping(String input) {
      return input.replace("\\", "\\\\")
              .replace("\n", "\\n")
              .replace("\t", "\\t")
              .replace("\0", "\\0")
              .replace("\"", "\\\"")
              .replace("'", "\\'");
    }

    @Override
    public List<String> visit(ExpString string) {
      return singleton("\"" + deEscaping(string.getValue()) +"\"");
    }

    @Override
    public List<String> visit(GlobalDeclaration globalDeclaration) {
      String declaration = stringFlatten((new Declaration(globalDeclaration)).accept(this));
      return singleton(globalDeclaration.getVisibility().toString() + " static " + declaration);
    }

    @Override
    public List<String> visit(Import imports) {
      return singleton("import " + imports.getPath());
    }

    @Override
    public List<String> visit(Function function) {
      String visibility = function.getVisibility().toString();
      String returnType = function.getReturn_type().isPresent() ?
        stringFlatten(function.getReturn_type().get().accept(this)) : "void";
      String name = function.getName();
      String arguments = visitFlattenNodes(function.getParameters());
      String header = visibility + " " + returnType + " " + name + parenthesis(arguments);
      List<String> block = function.getBody().accept(this);
      block.add(0, header);
      return block;
    }

    @Override
    public List<String> visit(TypeDefinition typeDefinition) {
      String name = typeDefinition.getName();
      List<String> fields = visitNodes(typeDefinition.getDeclarations());
      fields.add(0, "rec " + name + "{ ");
      fields.add("}");
      return fields;
    }

    @Override
    public List<String> visit(InsFor instruction) {
      String declaration = stringFlatten(instruction.getDeclaration().accept(this));
      String condition = stringFlatten(instruction.getRange().accept(this));
      String step = stringFlatten(instruction.getStep().accept(this));
      List<String> body = instruction.getBody().accept(this);
      body.add(0, "for" + parenthesis(declaration + "; " + condition + "; " + step));
      return body;
    }

    @Override
    public List<String> visit(InsWhile instruction) {
      String condition = "while" + parenthesis(stringFlatten(instruction.getCondition().accept(this)));
      List<String> body = instruction.getBody().accept(this);
      if (instruction.getDoWhile()) {
        body.add(0, "do");
        body.add(condition);
      } else
        body.add(0, condition);
      return body;
    }

    @Override
    public List<String> visit(InstForeach instruction) {
      String declaration = stringFlatten(instruction.getDeclaration().accept(this));
      String collection = stringFlatten(instruction.getCollection().accept(this));
      List<String> body = instruction.getBody().accept(this);
      body.add(0, "foreach" + parenthesis(declaration + spaceAround(":") + collection));
      return body;
    }

    @Override
    public List<String> visit(InsIf instruction) {
      List<String> result = new ArrayList<>();
      String condition = "if " + parenthesis(stringFlatten(instruction.getCondition().accept(this)));
      List<String> thenBranch = instruction.getBody().accept(this);
      result.add(condition);
      result.addAll(thenBranch);
      for (Pair<Expression, Instruction> conditionBranchPair : instruction.getElseif()) {
        result.add("elsif " + parenthesis(stringFlatten(conditionBranchPair.getFst().accept(this))));
        result.addAll(conditionBranchPair.getSnd().accept((this)));
      }
      if (instruction.getBodyElse().isPresent()) {
        result.add("else");
        result.addAll(instruction.getBodyElse().get().accept(this));
      }
      return result;
    }

    @Override
    public List<String> visit(InsAssign instruction) {
      String lValue = stringFlatten(instruction.getlValue().accept(this));
      String expression = stringFlatten(instruction.getExpression().accept(this));
      return singleton(lValue + spaceAround(instruction.getOperation().toString()) + expression);
    }

    @Override
    public List<String> visit(InsBlock instruction) {
      List<String> declarations = visitNodes(instruction.getDeclarations());
      List<String> instructions = visitNodes(instruction.getBody());
      List<String> result = new ArrayList<>();
      result.add("{");
      result.addAll(declarations);
      result.addAll(instructions);
      result.add("}");
      return result;
    }

    @Override
    public List<String> visit(InsBreak instruction) {
      return singleton("break");
    }

    @Override
    public List<String> visit(InsExpression instruction) {
      return singleton(stringFlatten(instruction.getExpression().accept(this)));
    }

    @Override
    public List<String> visit(TypPrimitive typPrimitive) {
      return singleton(typPrimitive.getType().toString());
    }

    @Override
    public List<String> visit(TypDico typDictionary) {
      return singleton(stringFlatten(typDictionary.getType().accept(this)) + " map");
    }

    @Override
    public List<String> visit(TypVariable typRecord) {
      return singleton(typRecord.getName());
    }

    @Override
    public List<String> visit(TypArray typArray) {
      String type = stringFlatten(typArray.getType().accept(this));
      return singleton(type + " array");
    }
  }
}
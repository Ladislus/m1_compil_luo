package ir.translation;

import ast.*;
import ir.Frame;
import ir.Register;
import ir.com.*;
import ir.expr.Binary;
import ir.expr.ReadMem;
import ir.expr.ReadReg;
import ir.expr.Unary;
import semantic_analysis.Signature;
import semantic_analysis.Signatures;
import semantic_analysis.SymbolTable;
import semantic_analysis.TypeChecker;
import support.Errors;
import support.ListTools;
import support.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Translate {
  public static final Errors errors = new Errors();
  private static final TypeConverter typeConverter = new TypeConverter();

  /**
   * Convert an Type object to an ir.Type.
   *
   * @param type a Type object
   * @return an IR type i.e. BYTE, INT or ADDRESS.
   */
  private static ir.Type ofType(ast.Type type) {
    return type.accept(typeConverter);
  }

  public static Pair<Label, List<Pair<Frame, List<Command>>>> run(SymbolTable symbolTable, Program program) {
    TranslationVisitor translator = new TranslationVisitor(symbolTable);
    program.accept(translator);
    return new Pair<>(translator.mainLabel, translator.fragments);
  }

  /**
   * A visitor of Type, to convert such a type to an ir.Type.
   */
  private static class TypeConverter extends ast.VisitorBase<ir.Type> {
    @Override
    public ir.Type visit(TypArray type) {
      return ir.Type.ADDRESS;
    }

    @Override
    public ir.Type visit(TypDico type) {
      return ir.Type.ADDRESS;
    }

    @Override
    public ir.Type visit(TypVariable type) {
      return ir.Type.ADDRESS;
    }

    @Override
    public ir.Type visit(TypPrimitive type) {
      switch (type.getType()) {
        case INT:
          return ir.Type.INT;
        case BOOL:
        case CHAR:
          return ir.Type.BYTE;
      }
      throw new Error("Translate: Unknown primitive type, please report");
    }

  }

  private static class TranslationVisitor implements ast.Visitor<ir.translation.Result> {

    private final SymbolTable symbolTable;
    private final TypeChecker typeChecker;

    private final Map<Pair<String, Integer>, Frame> frames;
    private final Map<Pair<InsBlock, String>, Register> varToReg;
    private final List<Pair<Frame, List<ir.com.Command>>> fragments;
    private Frame currentFrame;
    private Label mainLabel;

    public TranslationVisitor(SymbolTable symbolTable) {
      this.symbolTable = symbolTable;
      this.varToReg = new HashMap<>();
      this.typeChecker = new TypeChecker(symbolTable);
      this.fragments = new LinkedList<>();
      this.frames = new HashMap<>();
      this.currentFrame = null;
      this.mainLabel = null;
    }

    @Override
    public Result visit(ExpRecordAccess record) {
      //ToDo: TP8: implement or add an error "unsupported";
      return null;
    }

    @Override
    public Result visit(ExpRecordEnum enumeration) {
      //ToDo: TP8: implement or add an error "unsupported";
      return null;
    }

    @Override
    public Result visit(GlobalDeclaration globalDeclaration) {
      //ToDo: TP8: implement or add an error "unsupported";
      return null;
    }

    @Override
    public Result visit(Import imports) {
      errors.add("Translate: imports not supported " + imports.getPosition());
      return null;
    }

    @Override
    public Result visit(TypeDefinition typeDefinition) {
      //ToDo: TP8: implement or add an error "unsupported";
      return null;
    }

    @Override
    public Result visit(InsBreak instruction) {
      //ToDo: TP8: implement or add an error "unsupported";
      return null;
    }

    @Override
    public Result visit(TypPrimitive typPrimitive) {
      assert false : "Translate: internal error. This node should not be visited, please report";
      return null;
    }

    @Override
    public Result visit(TypDico typDictionary) {
      assert false : "Translate: internal error. This node should not be visited, please report";
      return null;
    }

    @Override
    public Result visit(TypVariable typRecord) {
      assert false : "Translate: internal error. This node should not be visited, please report";
      return null;
    }

    @Override
    public Result visit(TypArray typArray) {
      assert false : "Translate: internal error. This node should not be visited, please report";
      return null;
    }

    private Pair<InsBlock, String> inCurrentBlock(String variable) {
      return new Pair<>(typeChecker.getVisitedBlocks().current(), variable);
    }

    @Override
    public Result visit(ExpBoolean exp) {
      return new Result(new ir.expr.Byte((byte) (exp.getValue() ? 1 : 0)));
    }

    @Override
    public Result visit(ExpCharacter exp) {
      return new Result(new ir.expr.Byte((byte) exp.getValue()));
    }

    @Override
    public Result visit(ExpInteger exp) {
      return new Result(new ir.expr.Int(exp.getValue()));
    }


    private Register registerLookup(String name) {
      Register reg;
      for (InsBlock block : typeChecker.getVisitedBlocks().getStack()) {
        reg = varToReg.get(new Pair<>(block, name));
        if (reg != null) return reg;
      }
      assert false : "Internal Error: no register associated with " + name;
      return null;
    }

    @Override
    public Result visit(ExpVariable exp) {
      Register reg = registerLookup(exp.getVariable());
      return new Result(new ReadReg(reg));
    }

    @Override
    public Result visit(ExpBinaryOperation exp) {
      Result left = exp.getLeft().accept(this);
      Result right = exp.getRight().accept(this);
      ir.expr.Expression binary = new Binary(left.getExp(), right.getExp(), exp.getOperator());
      List<ir.com.Command> code = new LinkedList<>();
      code.addAll(left.getCode());
      code.addAll(right.getCode());
      return new Result(binary, code);
    }

    @Override
    public Result visit(ExpUnaryOperation exp) {
      Result result = exp.getExpression().accept(this);
      EnumUnaryOp op = exp.getOperator();
      switch (op) {
        case DEC:
        case INC:
          errors.add("Translate: ++ and -- are unsupported");
      }
      ir.expr.Expression newExp = new Unary(result.getExp(), exp.getOperator());
      return new Result(newExp, result.getCode());
    }

    @Override
    public Result visit(ExpArrayAccess exp) {
      Optional<Type> typingResult = exp.accept(typeChecker);
      assert typingResult.isPresent() : "Internal Error: typing failed in " + exp;
      Type cellType = typingResult.get();
      Result arrayResult = exp.getArray().accept(this);
      Result indexResult = exp.getIndex().accept(this);
      Register arrayRegister = new Register(ir.Type.ADDRESS);
      currentFrame.addLocal(arrayRegister);
      List<ir.com.Command> code = new LinkedList<>(indexResult.getCode());
      code.addAll(arrayResult.getCode());
      ir.expr.Expression index = indexResult.getExp();
      ir.com.Command arrayCom = new WriteReg(arrayRegister, arrayResult.getExp());
      code.add(arrayCom);
      ir.expr.Expression newExp = new ReadMem(arrayRegister, index, ofType(cellType));
      return new Result(newExp, code);
    }

    @Override
    public Result visit(InsAssign stm) {
      // InsAssign has passed the type checking, therefore the lValue field
      // contains either an ExpVar, or and ExpArrAccess expression
      if (!stm.getOperation().equals(EnumAssignOp.EQUAL))
        errors.add("Translate: assignment with operator " + stm.getPosition());
      assert (stm.getlValue() instanceof ast.ExpVariable ||
        stm.getlValue() instanceof ast.ExpArrayAccess ||
        stm.getlValue() instanceof ast.ExpRecordAccess) :
        "Internal Error: lValue not ExpVar or ExpArrayAccess or ExpRecordAccess in " + stm;
      Result resultExp = stm.getExpression().accept(this);
      List<ir.com.Command> code = new LinkedList<>(resultExp.getCode());
      if (stm.getlValue() instanceof ast.ExpVariable) {
        ast.ExpVariable expVar = (ast.ExpVariable) stm.getlValue();
        Register reg = registerLookup(expVar.getVariable());
        assert reg != null : "Internal Error: no register associated with " + expVar.getVariable();
        code.add(new ir.com.WriteReg(reg, resultExp.getExp()));
      }
      if (stm.getlValue() instanceof ast.ExpArrayAccess) {
        Result result = stm.getlValue().accept(this);
        if (result.getExp() instanceof ReadMem) {
          code.addAll(result.getCode());
          ReadMem read = (ReadMem) result.getExp();
          Optional<Type> typingResult = stm.getExpression().accept(typeChecker);
          assert typingResult.isPresent() : "Internal Error: typing failed in " + stm.getExpression();
          ir.Type type = ofType(typingResult.get());
          code.add(new ir.com.WriteMem(read.getRegister(), read.getOffset(), resultExp.getExp(), type));
        } else
          assert false : "Internal error: ExpArrAccess not translated to ReadMem in " + stm.getlValue();
      }
      //ToDo: TP8 implement record access in write mode or error "unsupported"
      return new Result(code);
    }

    @Override
    public Result visit(InsExpression stm) {
      return stm.getExpression().accept(this);
    }

    @Override
    public Result visit(InsReturn stm) {
      Result result = stm.getExpression().accept(this);
      assert currentFrame.getResult().isPresent() :
        "Internal Error: return statement in a function without a return type in " + stm;
      Register returnReg = currentFrame.getResult().get();
      Command writeReturnReg = new WriteReg(returnReg, result.getExp());
      Command gotoExitPoint = new Jump(currentFrame.getExitPoint());
      List<Command> code = new LinkedList<>(result.getCode());
      code.add(writeReturnReg);
      code.add(gotoExitPoint);
      return new Result(code);
    }

    private Pair<List<ir.expr.Expression>, List<Command>> translateExpressions(List<Expression> exps) {
      List<Command> code = new LinkedList<>();
      List<ir.expr.Expression> expressions = new LinkedList<>();
      for (Expression expression : exps) {
        Result result = expression.accept(this);
        expressions.add(result.getExp());
        code.addAll(result.getCode());
      }
      return new Pair<>(expressions, code);
    }

    private Result makeFunCall(Type type, Frame frame,
                               List<ir.expr.Expression> arguments,
                               List<Command> code) {
      Register reg = new Register(ofType(type));
      currentFrame.addLocal(reg);
      ir.com.Command call = new FunCall(reg, frame, arguments);
      code.add(call);
      return new Result(new ReadReg(reg), code);
    }

    private Result makeProcCall(Frame frame, List<ir.expr.Expression> arguments,
                                List<Command> code) {
      code.add(new ProcCall(frame, arguments));
      return new Result(code);
    }

    List<Type> getArgumentTypes(List<Expression> arguments) {
      return arguments.stream()
        .map((a) -> a.accept(typeChecker))
        .map(Optional::get)
        .collect(Collectors.toList());
    }

    int indexOfSignature(List<Type> argumentsTypes, List<Signature> signatures) {
      for (int index = 0; index < signatures.size(); index++)
        if (argumentsTypes.equals(signatures.get(index).getArgumentsTypes()))
          return index;
      return -1;
    }

    @Override
    public Result visit(ExpFunctionCall exp) {
      Pair<List<ir.expr.Expression>, List<Command>> translation =
        translateExpressions(exp.getArguments());
      List<ir.com.Command> code = translation.getSnd();
      List<ir.expr.Expression> arguments = translation.getFst();
      List<Signature> signaturesForThisName = symbolTable.functionLookup(exp.getName());
      assert !signaturesForThisName.isEmpty() : "Internal Error: function name not in symbol table: " + exp.getName();

      String functionName = exp.getName();
      List<Type> argumentTypes = getArgumentTypes(exp.getArguments());
      int index = indexOfSignature(argumentTypes, signaturesForThisName);
      assert index != -1 : "Internal Error: no signature with these argument types " + exp.getPosition();
      Signature signature = signaturesForThisName.get(index);
      Optional<Type> returnType = signature.getReturnType();

      Frame frame = frames.get(new Pair<>(functionName, index));
      return returnType
        .map(type -> makeFunCall(type, frame, arguments, code))
        .orElseGet(() -> makeProcCall(frame, arguments, code));
    }

    private Result newArray(Type cellType, Result length) {
      Register arrayReg = new Register(ir.Type.ADDRESS);
      currentFrame.addLocal(arrayReg);
      ir.Type type = ofType(cellType);
      ir.expr.Expression cellSize = type.toSymbol();
      List<Command> code = length.getCode();
      code.add(new FunCall(arrayReg, PredefinedFrames.NEW,
        ListTools.two(cellSize, length.getExp())));
      return new Result(new ReadReg(arrayReg), code);
    }

    @Override
    public Result visit(ExpNew exp) {
      Result length = exp.getArgument().accept(this);
      return newArray(exp.getType(), length);
    }

    @Override
    public Result visit(ExpArrayEnum array) {
      int size = array.getElements().size();
      if (size == 0)
        errors.add("Translate: array enumeration of size 0 unsupported " + array.getPosition());
      Optional<Type> optType = array.getElements().get(0).accept(typeChecker);
      assert optType.isPresent() :
        "Internal Error: no type for first element of enumeration " + array.getPosition();
      Type type = optType.get();
      Result arrayCreation = newArray(type, new Result(new ir.expr.Int(size)));
      Register arrayReg = ((ReadReg) arrayCreation.getExp()).getRegister();
      List<Command> code = arrayCreation.getCode();
      for (int counter = 0; counter < size; counter++) {
        Result elementResult = array.getElements().get(counter).accept(this);
        code.addAll(elementResult.getCode());
        code.add(new WriteMem(arrayReg, new ir.expr.Int(counter),
          elementResult.getExp(), elementResult.getExp().getType()));
      }
      return new Result(new ReadReg(arrayReg), code);
    }

    private ExpArrayEnum enumOfString(ExpString exp) {
      List<Expression> exps = new LinkedList<>();
      for (char c : exp.getValue().toCharArray())
        exps.add(new ExpCharacter(exp.getPosition(), c));
      exps.add(new ExpCharacter(exp.getPosition(), '\0'));
      return new ExpArrayEnum(exp.getPosition(), exps);
    }

    @Override
    public Result visit(ExpString exp) {
      return enumOfString(exp).accept(this);
    }

    @Override
    public Result visit(InsIf stm) {
      //ToDo: TP8 to complete
      Result condition = stm.getCondition().accept(this);
      Label thenLabel = Label.fresh();
      Label elseLabel = Label.fresh();
      Label exitLabel = Label.fresh();
      List<Command> thenBranch = stm.getBody().accept(this).getCode();
      boolean hasElse = stm.getBodyElse().isPresent();
      List<Command> elseBranch = hasElse ?
        stm.getBodyElse().get().accept(this).getCode() :
        new LinkedList<>();
      Command conditional = new ir.com.CJump(condition.getExp(), thenLabel, elseLabel);
      List<Command> code = condition.getCode();
      code.add(conditional);
      code.add(thenLabel);
      code.addAll(thenBranch);
      if (hasElse) code.add(new Jump(exitLabel));
      code.add(elseLabel);
      code.addAll(elseBranch);
      if (hasElse) code.add(exitLabel);
      return new Result(code);
    }


    @Override
    public Result visit(InsWhile stm) {
      Label loopEntry = Label.fresh();
      Label loopBody = Label.fresh();
      Label loopExit = Label.fresh();
      Result conditionTranslation = stm.getCondition().accept(this);
      List<Command> bodyCode = stm.getBody().accept(this).getCode();
      List<Command> code = ListTools.singleton(loopEntry);
      if (!stm.getDoWhile()) {
        code.addAll(conditionTranslation.getCode());
        code.add(new CJump(conditionTranslation.getExp(),
          loopBody, loopExit));
        code.add(loopBody);
      }
      code.addAll(bodyCode);
      if (stm.getDoWhile()) {
        code.addAll(conditionTranslation.getCode());
        code.add(new CJump(conditionTranslation.getExp(),
          loopBody, loopExit));
        code.add(loopBody);
      }
      code.add(new Jump(loopEntry));
      code.add(loopExit);
      return new Result(code);
    }


    @Override
    public Result visit(InsFor stm) {
      errors.add("Translate: for loop unsupported");
      return null;
    }

    @Override
    public Result visit(InstForeach stm) {
      errors.add("Translate: foreach loop unsupported");
      return null;
    }

    @Override
    public Result visit(Declaration stm) {
      // Declaration of a new variable = creation of a fresh register
      Register reg = new Register(ofType(stm.getType()));
      varToReg.put(inCurrentBlock(stm.getVariable()), reg);
      currentFrame.addLocal(reg);
      // If there is an initialization, a write to the new register is needed
      List<ir.com.Command> code = new LinkedList<>();
      if (stm.getExpression().isPresent()) {
        Result result = stm.getExpression().get().accept(this);
        code.addAll(result.getCode());
        code.add(new ir.com.WriteReg(reg, result.getExp()));
      }
      return new Result(code);
    }

    @Override
    public Result visit(InsBlock block) {
      typeChecker.getVisitedBlocks().enter(block);
      List<Command> code = new LinkedList<>();
      for (Declaration declaration : block.getDeclarations()) {
        Result result = declaration.accept(this);
        code.addAll(result.getCode());
      }
      for (Instruction stm : block.getBody()) {
        Result result = stm.accept(this);
        code.addAll(result.getCode());
      }
      typeChecker.getVisitedBlocks().exit();
      return new Result(code);
    }

    @Override
    public Result visit(Function fun) {
      Pair<String, Integer> key = getFunctionKey(fun);
      Frame frame = frames.get(key);
      assert frame != null : "Internal Error: no frame for function " + key;
      currentFrame = frame;
      Result result = fun.getBody().accept(this);
      // If result contains an expression part, it is just discarded
      fragments.add(new Pair<>(frame, result.getCode()));
      return null;
    }

    @Override
    public Result visit(Program program) {
      program.accept(typeChecker);
      FramesBuilder framesBuilder = new FramesBuilder();
      program.accept(framesBuilder);
      for (Function fun : program.getFunctions())
        fun.accept(this);
      return null;
    }

    private Pair<String, Integer> getFunctionKey(Function function) {
      return getFunctionKeyByNameSignature(function.getName(), Signature.signatureOf(function));
    }

    private Pair<String, Integer> getFunctionKeyByNameSignature(String functionName, Signature signature) {
      List<Signature> signaturesForThisName = symbolTable.functionLookup(functionName);
      int index = signaturesForThisName.indexOf(signature);
      return new Pair<>(functionName, index);
    }

    private class FramesBuilder extends ast.VisitorBase<Void> {

      @Override
      public Void visit(Function function) {
        List<Register> parameters = new LinkedList<>();
        for (Declaration argument : function.getParameters()) {
          if (argument.getExpression().isPresent())
            errors.add("Translate: parameter with default value not supported " + argument.getPosition());
          ast.Type argType = argument.getType();
          String argName = argument.getVariable();
          Register register = new Register(ofType(argType));
          varToReg.put(new Pair<>(function.getBody(), argName), register);
          parameters.add(register);
        }
        Frame frame;
        if (function.getReturn_type().isPresent()) {
          Type type = function.getReturn_type().get();
          ir.Type irType = ofType(type);
          frame = new Frame(Label.fresh(), Label.fresh(), parameters, new Register(irType));
        } else
          frame = new Frame(Label.fresh(), Label.fresh(), parameters);

        frames.put(getFunctionKey(function), frame);
        if (function.getName().equals("main"))
          mainLabel = frame.getEntryPoint();
        return null;
      }

      private void addPredefinedFunctions() {
        frames.put(getFunctionKeyByNameSignature("print", Signatures.printChar), PredefinedFrames.PRINT_CHAR);
        frames.put(getFunctionKeyByNameSignature("print", Signatures.printInt), PredefinedFrames.PRINT_INT);
        frames.put(getFunctionKeyByNameSignature("print", Signatures.printBool), PredefinedFrames.PRINT_BOOL);
        frames.put(getFunctionKeyByNameSignature("print", Signatures.printString), PredefinedFrames.PRINT_STRING);
      }

      @Override
      public Void visit(Program program) {
        for (Function function : program.getFunctions())
          function.accept(this);
        addPredefinedFunctions();
        return null;
      }
    }
  }
}

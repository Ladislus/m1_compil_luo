package semantic_analysis;

import java.util.List;
import java.util.Map;

public class Signatures {
  //ToDo: to complete see TP6
  //      and class src/main/java/compiler/Signature.java in W2.zip

  public static final Signature printChar = Signature.buildUnaryVoid(Signature.CHAR);
  public static final Signature printInt = Signature.buildUnaryVoid(Signature.INT);
  public static final Signature printString = Signature.buildUnaryVoid(Signature.STRING);
  public static final Signature printBool = Signature.buildUnaryVoid(Signature.BOOL);

  public static void addPredefinedSignature(SymbolTable symbolTable){
    symbolTable.addFunction("print", printChar);
    symbolTable.addFunction("print", printInt);
    symbolTable.addFunction("print", printBool);
    symbolTable.addFunction("print", printString);
  }
}

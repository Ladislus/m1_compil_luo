package semantic_analysis;

import ast.EnumBinaryOp;
import ast.EnumPredefinedOp;
import ast.EnumUnaryOp;

import java.util.HashMap;
import java.util.Map;

public class Signatures {
    public static final Map<EnumBinaryOp, Signature> binary = buildBinary();
    public static final Map<EnumUnaryOp, Signature> unary = buildUnary();
    public static final Map<EnumPredefinedOp, Signature> premade = buildPremade();

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

    private static Map<EnumBinaryOp, Signature> buildBinary() {
        Map<EnumBinaryOp, Signature> mapping = new HashMap<>();
        mapping.put(EnumBinaryOp.ADD, Signature.binaryArithmetic);
        mapping.put(EnumBinaryOp.SUB, Signature.binaryArithmetic);
        mapping.put(EnumBinaryOp.MUL, Signature.binaryArithmetic);
        mapping.put(EnumBinaryOp.DIV, Signature.binaryArithmetic);
        mapping.put(EnumBinaryOp.MOD, Signature.binaryArithmetic);
        mapping.put(EnumBinaryOp.AND, Signature.binaryBoolean);
        mapping.put(EnumBinaryOp.OR, Signature.binaryBoolean);
        mapping.put(EnumBinaryOp.LT, Signature.comparison);
        mapping.put(EnumBinaryOp.LTE, Signature.comparison);
        mapping.put(EnumBinaryOp.GT, Signature.comparison);
        mapping.put(EnumBinaryOp.GTE, Signature.comparison);
        // EQ and NEQ not there: they'd need a polymorphic signature
        return mapping;
    }

    private static Map<EnumUnaryOp, Signature> buildUnary() {
        Map<EnumUnaryOp, Signature> mapping = new HashMap<>();
        mapping.put(EnumUnaryOp.DEC, Signature.unaryArithmetic);
        mapping.put(EnumUnaryOp.INC, Signature.unaryArithmetic);
        mapping.put(EnumUnaryOp.MINUS, Signature.unaryArithmetic);
        mapping.put(EnumUnaryOp.NOT, Signature.unaryBoolean);
        return mapping;
    }

    private static Map<EnumPredefinedOp, Signature> buildPremade() {
        Map<EnumPredefinedOp, Signature> mapping = new HashMap<>();
        mapping.put(EnumPredefinedOp.INCHAR, Signature.buildUnary(Signature.Void, Signature.Char));
        mapping.put(EnumPredefinedOp.INSTRING, Signature.buildUnary(Signature.Void, Signature.String));
        mapping.put(EnumPredefinedOp.ININT, Signature.buildUnary(Signature.Void, Signature.Integer));
        mapping.put(EnumPredefinedOp.INBOOL, Signature.buildUnary(Signature.Void, Signature.Bool));
        mapping.put(EnumPredefinedOp.OUTCHAR, Signature.buildUnary(Signature.Char, Signature.Void));
        mapping.put(EnumPredefinedOp.OUTSTRING, Signature.buildUnary(Signature.String, Signature.Void));
        mapping.put(EnumPredefinedOp.OUTINT, Signature.buildUnary(Signature.Integer, Signature.Void));
        mapping.put(EnumPredefinedOp.OUTBOOL, Signature.buildUnary(Signature.Bool, Signature.Void));
        mapping.put(EnumPredefinedOp.INTTOCHAR, Signature.buildUnary(Signature.Integer, Signature.Char));
        mapping.put(EnumPredefinedOp.INTTOBOOL, Signature.buildUnary(Signature.Integer, Signature.Bool));
        mapping.put(EnumPredefinedOp.CHARTOINT, Signature.buildUnary(Signature.Char, Signature.Integer));
        mapping.put(EnumPredefinedOp.BOOLTOINT, Signature.buildUnary(Signature.Bool, Signature.Integer));
        mapping.put(EnumPredefinedOp.ASSERT, Signature.buildBinary(Signature.Bool, Signature.String, Signature.Void));
        // FREE and LENGTH not there: they'd need a polymorphic signature
        return mapping;
    }
}

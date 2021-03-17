package semantic_analysis;

import ast.EnumUnaryOp;
import ast.EnumBinaryOp;

import java.util.HashMap;
import java.util.Map;

// TODO ajouter fonctions prédéfinies
public class Signatures {
    public static final Map<EnumBinaryOp, Signature> binary = buildBinary();
    public static final Map<EnumUnaryOp, Signature> unary = buildUnary();

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
}

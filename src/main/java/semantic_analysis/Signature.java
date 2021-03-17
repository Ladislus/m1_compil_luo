package semantic_analysis;

import ast.EnumPrimitiveType;
import ast.Position;
import ast.TypPrimitive;
import ast.Type;

import java.util.ArrayList;
import java.util.List;

public class Signature {

    private static final Position placeholderPosition = new Position(-1, -1);
    public static final TypPrimitive Integer = new TypPrimitive(placeholderPosition, EnumPrimitiveType.INT);
    public static final TypPrimitive Bool = new TypPrimitive(placeholderPosition, EnumPrimitiveType.BOOL);
    public static final TypPrimitive Char = new TypPrimitive(placeholderPosition, EnumPrimitiveType.CHAR);

    public final static Signature binaryArithmetic =
            buildBinary(Integer,
                        Integer,
                        Integer);
    public final static Signature binaryBoolean =
            buildBinary(Bool,
                        Bool,
                        Bool);
    public final static Signature unaryArithmetic =
            buildUnary(Integer,
                       Integer);
    public final static Signature unaryBoolean =
            buildUnary(Bool,
                       Bool);
    public final static Signature comparison =
            buildBinary(Integer,
                        Integer,
                        Bool);

    public List<Type> argTypes;
    public Type returnType;

    public Signature(List<Type> argTypes,
                      Type returnType) {
        this.argTypes = argTypes;
        this.returnType = returnType;
    }

    public static Signature buildBinary(Type t1, Type t2, Type rt) {
        List<Type> argTypes = new ArrayList<>();
        argTypes.add(t1);
        argTypes.add(t2);
        return new Signature(argTypes, rt);
    }

    public static Signature buildUnary(Type type,
                                        Type rt) {
        List<Type> argTypes = new ArrayList<>();
        argTypes.add(type);
        return new Signature(argTypes, rt);
    }
}

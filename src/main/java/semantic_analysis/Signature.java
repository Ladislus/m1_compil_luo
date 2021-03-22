package semantic_analysis;

import ast.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Signature {

    private static final Position placeholderPosition = new Position(-1, -1);
    public static final Type Integer = new TypPrimitive(placeholderPosition, EnumPrimitiveType.INT);
    public static final Type Bool = new TypPrimitive(placeholderPosition, EnumPrimitiveType.BOOL);
    public static final Type Char = new TypPrimitive(placeholderPosition, EnumPrimitiveType.CHAR);
    public static final Type Void = new TypVariable(placeholderPosition, "void");
    public static final Type String = new TypArray(placeholderPosition, Signature.Char);

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signature signature = (Signature) o;
        return argTypes.equals(signature.argTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(argTypes);
    }
}

package semantic_analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ast.*;

public class Signature {
    private final List<Type> argumentsTypes;
    private final Optional<Type> returnType;

    private static final Position placeholderPosition = new Position(-1, -1);
    public static final Type Integer = new TypPrimitive(placeholderPosition, EnumPrimitiveType.INT);
    public static final Type Bool = new TypPrimitive(placeholderPosition, EnumPrimitiveType.BOOL);
    public static final Type Char = new TypPrimitive(placeholderPosition, EnumPrimitiveType.CHAR);
    public static final Type Void = new TypVariable(placeholderPosition, "void");
    public static final Type String = new TypArray(placeholderPosition, Signature.Char);

    public List<Type> getArgumentsTypes() {
        return argumentsTypes;
    }

    public Optional<Type> getReturnType() {
        return returnType;
    }

    public Signature(List<Type> argumentsTypes, Optional<Type> returnType) {
        this.argumentsTypes = argumentsTypes;
        this.returnType = returnType;
    }

    public static Signature buildBinary(Type t1, Type t2, Type rt) {
        List<Type> argTypes = new ArrayList<>();
        argTypes.add(t1);
        argTypes.add(t2);
        return new Signature(argTypes, Optional.of(rt));
    }

    public static Signature buildUnary(Type type, Type rt) {
        List<Type> argTypes = new ArrayList<>();
        argTypes.add(type);
        return new Signature(argTypes, Optional.of(rt));
    }

    public final static Signature binaryArithmetic =
            buildBinary(Integer, Integer, Integer);
    public final static Signature binaryBoolean =
            buildBinary(Bool, Bool, Bool);
    public final static Signature unaryArithmetic =
            buildUnary(Integer, Integer);
    public final static Signature unaryBoolean =
            buildUnary(Bool, Bool);
    public final static Signature comparison =
            buildBinary(Integer, Integer, Bool);

    public boolean check(List<Type> types) {
        if (types.size() == argumentsTypes.size()) {
            for (Integer counter = 0; counter < types.size(); counter++)
                if (!types.get(counter).equals(argumentsTypes.get(counter)))
                    return false;
            return true;
        }
        return false;
    }

    public boolean check(Type type) {
        List<Type> types = new ArrayList<>();
        types.add(type);
        return check(types);
    }

    public boolean check(Type t1, Type t2) {
        List<Type> types = new ArrayList<>();
        types.add(t1);
        types.add(t2);
        return check(types);
    }

    public static Signature signatureOf(Function function) {
        List<Type> argumentTypes =
                function.getParameters().stream()
                        .map(Declaration::getType)
                        .collect(Collectors.toList());
        Optional<Type> returnType = function.getReturn_type();
        return new Signature(argumentTypes, returnType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signature signature = (Signature) o;
        return argumentsTypes.equals(signature.argumentsTypes) && returnType.equals(signature.returnType);
    }
}

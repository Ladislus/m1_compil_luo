package mips;

import ir.Type;
import support.ListTools;
import java.util.List;

public class Asm {

    static int sizeOf(Type type) {
        if (type == Type.BYTE) return 1;
        return 4;
    }

    static String command(String command) {
        return "\t" + command;
    }

    static String label(String label) {
        return label + ":";
    }

    static String directive(String directive) {
        return command("." + directive);
    }

    static List<String> exit() {
        List<String> asmCode = ListTools.singleton(command("li $v0, 10"));
        asmCode.add(command("syscall"));
        return asmCode;
    }

    static List<String> push(String register) {
        List<String> asmCode = ListTools.singleton(command("sub $sp, 4"));
        asmCode.add(command("sw " + register + ", 4($sp)"));
        return asmCode;
    }

    static List<String> pop(String register) {
        List<String> asmCode = ListTools.singleton(command("lw " + register + ", 4($sp)"));
        asmCode.add(command("add $sp, 4"));
        return asmCode;
    }

    private static String memoryOperation(int size, String op) {
        assert (op.equals("l") || op.equals("s"));
        return switch (size) {
            case 1 -> op + "b";
            case 2 -> op + "h";
            default -> op + "w";
        };
    }

    public static String load(int size) {
        return memoryOperation(size, "l");
    }

    public static String save(int size) {
        return memoryOperation(size, "s");
    }
}
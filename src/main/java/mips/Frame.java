package mips;

import ir.Register;
import support.Errors;
import support.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Frame {

    private final Errors errorReporter;

    public Frame(Errors errorReporter) {
        this.errorReporter = errorReporter;
    }

    List<Register> allFromFrame(ir.Frame frame) {
        List<Register> registers = new LinkedList<>(frame.getParameters());
        registers.addAll(frame.getLocals());
        if (frame.getResult().isPresent())
            registers.add(frame.getResult().get());
        return registers;
    }

    private void checkNumParameters(ir.Frame frame) {
        if (frame.getParameters().size() > 4)
            errorReporter.add("functions with more than 4 arguments");
    }

    private void updateRegAlloc(ir.Frame frame, Map<Register, Integer> regAlloc) {
        List<Register> registers = allFromFrame(frame);
        int offset = 0;
        for (Register register : registers) {
            regAlloc.put(register, offset);
            offset -= Program.DEFAULT_SIZE;
        }
        frame.setSize(Program.DEFAULT_SIZE * (registers.size() + 2));
    }

    private List<String> generateBody(Map<Register, Integer> regAlloc,
                                      List<ir.com.Command> fragment) {
        Command comVisitor = new Command(errorReporter, regAlloc);
        List<String> asmCode = new LinkedList<>();
        for (ir.com.Command com : fragment)
            asmCode.addAll(com.accept(comVisitor));
        return asmCode;
    }

    private List<String> generatePrologue(Map<Register, Integer> regAlloc,
                                          ir.Frame frame) {
        List<String> asmCode = new LinkedList<>();
        asmCode.add(Asm.label(frame.getEntryPoint().toString()));
        // 1. backup of caller's $ra and $fp registers.
        asmCode.add(Asm.command("move $t0, $fp"));
        asmCode.add(Asm.command("move $t1, $ra"));
        // 2. update $fp and $sp.
        asmCode.add(Asm.command("move $fp, $sp"));
        asmCode.add(Asm.command("addi $sp, $sp, -" + frame.getSize()));
        asmCode.add(Asm.command("sw $t0, 8($sp)"));
        asmCode.add(Asm.command("sw $t1, 4($sp)"));
        // 3. copy registers a0, ..., a3 in the corresponding temporary registers.
        int counter = 0;
        for (Register reg : frame.getParameters()) {
            asmCode.add(Asm.command("sw $a" + counter
                    + ", " + regAlloc.get(reg) + "($fp)"));
            counter += 1;
        }
        return asmCode;
    }

    private List<String> generateEpilogue(Map<Register, Integer> regAlloc,
                                          ir.Frame frame) {
        List<String> asmCode = new LinkedList<>();
        int fpOffset = 8 - frame.getSize();
        int raOffset = 4 - frame.getSize();
        asmCode.add(Asm.label(frame.getExitPoint().toString()));
        if (frame.getResult().isPresent()) {
            int registerOffset = regAlloc.get(frame.getResult().get());
            asmCode.add(Asm.command("lw $v0, " + registerOffset + "($fp)"));
        }
        asmCode.add(Asm.command("move $sp, $fp"));
        asmCode.add(Asm.command("lw $ra, " + raOffset + "($fp)"));
        asmCode.add(Asm.command("lw $fp, " + fpOffset + "($fp)"));
        asmCode.add(Asm.command("j $ra"));
        return asmCode;
    }

    List<String> generate(Pair<ir.Frame, List<ir.com.Command>> fragment) {
        ir.Frame frame = fragment.getFst();
        Map<Register, Integer> regAlloc = new HashMap<>();
        checkNumParameters(frame);
        updateRegAlloc(frame, regAlloc);
        List<String> asmCode = generatePrologue(regAlloc, frame);
        asmCode.addAll(generateBody(regAlloc, fragment.getSnd()));
        asmCode.addAll(generateEpilogue(regAlloc, frame));
        return asmCode;
    }
}

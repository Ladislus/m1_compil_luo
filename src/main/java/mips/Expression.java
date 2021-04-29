package mips;

import ir.Register;
import ir.RegisterOffset;
import ir.Type;
import ir.com.Label;
import ir.expr.Byte;
import ir.expr.*;
import support.ListTools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static mips.Asm.sizeOf;

public class Expression implements Visitor<List<String>> {

    private final Map<Register, Integer> regAddress;

    public Expression(Map<Register, Integer> regAddress) {
        this.regAddress = regAddress;
    }

    @Override
    public List<String> visit(Byte exp) {
        List<String> asmCode =
                ListTools.singleton(Asm.command("li $t0, " + exp.getValue()));
        asmCode.addAll(Asm.push("$t0"));
        return asmCode;
    }

    @Override
    public List<String> visit(Int exp) {
        List<String> asmCode =
                ListTools.singleton(Asm.command("li $t0, " + exp.getValue()));
        asmCode.addAll(Asm.push("$t0"));
        return asmCode;
    }

    @Override
    public List<String> visit(ReadReg exp) {
        int offset = regAddress.get(exp.getRegister());
        List<String> asmCode = new LinkedList<>();
        asmCode.add(Asm.command("lw $t0, " + offset + "($fp)"));
        asmCode.addAll(Asm.push("$t0"));
        return asmCode;
    }

    public List<String> memoryRW(RegisterOffset irObject, int size){
        Register arrayRegister = irObject.getRegister();
        int registerOffset = regAddress.get(arrayRegister);
        // The offset is an expression: the value is pushed
        List<String> asmCode = irObject.getOffset().accept(this);
        // The offset is popped in $t2
        asmCode.addAll(Asm.pop("$t2"));
        // The address of the array is in $t1
        asmCode.add(Asm.command(Asm.load(Program.DEFAULT_SIZE)
                + " $t1, " + registerOffset + "($fp)"));
        asmCode.add(Asm.command("li $t3, " + size));
        asmCode.add(Asm.command("mul $t2, $t2, $t3"));
        asmCode.add(Asm.command("add $t1, $t1, $t2"));
        asmCode.add(Asm.command("li $t3, " + sizeOf(Type.INT)));
        // The address of the cell is in $t1
        asmCode.add(Asm.command("add $t1, $t1, $t3"));
        return asmCode;
    }

    @Override
    public List<String> visit(ReadMem exp) {
        int size = sizeOf(exp.getType());
        List<String> asmCode = memoryRW(exp, size);
        asmCode.add(Asm.command(Asm.load(size) + " $t0, ($t1)"));
        asmCode.addAll(Asm.push("$t0"));
        return asmCode;
    }

    @Override
    public List<String> visit(Unary exp) {
        List<String> code = exp.getExp().accept(this);
        code.addAll(Asm.pop("$t0"));
        switch (exp.getOp()) {
            case MINUS:
                code.add(Asm.command("subu $t0, $zero, $t0"));
                break;
            case NOT:
                code.add(Asm.command("li $t1, 4294967294"));
                code.add(Asm.command("nor $t0, $t1, $t0"));
        }
        code.addAll(Asm.push("$t0"));
        return code;
    }

    @Override
    public List<String> visit(Binary exp) {
        exp.getLeft().accept(this);
        List<String> asmCode = new ArrayList<>(Asm.pop("$t0"));
        exp.getRight().accept(this);
        asmCode.addAll(Asm.pop("$t1"));
        switch (exp.getOp()) {
            case ADD -> asmCode.add(Asm.command("add $t2, $t0, $t1"));
            case SUB -> asmCode.add(Asm.command("sub $t2, $t0, $t1"));
            case MUL -> {
                asmCode.add(Asm.command("mult $t0, $t1"));
                // Si mfhi > 0, alors le résultat fait plus de 32 bits, donc edge case
                asmCode.add(Asm.command("mflo $t2"));
            }
            case DIV -> {
                asmCode.add(Asm.command("div $t0, $t1"));
                // Si mfhi contient le reste, mais division entière
                asmCode.add(Asm.command("mflo $t2"));
            }
            case MOD -> {
                asmCode.add(Asm.command("div $t0, $t1"));
                // mfhi contient le reste
                asmCode.add(Asm.command("mfhi $t2"));
            }
            case EQ -> {
                Label ok = Label.fresh();
                Label exit = Label.fresh();
                asmCode.add(Asm.command("beq $t0, $t1, " + ok));
                asmCode.add(Asm.command("li $t2, 0"));
                asmCode.add(Asm.command("j " + exit));
                asmCode.add(Asm.label(ok.toString()));
                asmCode.add(Asm.command("li $t2, 1"));
            }
            case DIFF -> {
                Label ok = Label.fresh();
                Label exit = Label.fresh();
                asmCode.add(Asm.command("beq $t0, $t1, " + ok));
                asmCode.add(Asm.command("li $t2, 1"));
                asmCode.add(Asm.command("j " + exit));
                asmCode.add(Asm.label(ok.toString()));
                asmCode.add(Asm.command("li $t2, 0"));
            }
            case GT -> {
                Label ok = Label.fresh();
                Label exit = Label.fresh();
                asmCode.add(Asm.command("sub $t3, $t0, $t1"));
                asmCode.add(Asm.command("bgtz " + ok));
                asmCode.add(Asm.command("li $t2, 0"));
                asmCode.add(Asm.command("j " + exit));
                asmCode.add(Asm.label(ok.toString()));
                asmCode.add(Asm.command("li $t2, 1"));
            }
            case GTE -> {
                Label ok = Label.fresh();
                Label exit = Label.fresh();
                asmCode.add(Asm.command("sub $t3, $t0, $t1"));
                asmCode.add(Asm.command("bgez " + ok));
                asmCode.add(Asm.command("li $t2, 0"));
                asmCode.add(Asm.command("j " + exit));
                asmCode.add(Asm.label(ok.toString()));
                asmCode.add(Asm.command("li $t2, 1"));
            }
            case LT -> {
                Label ok = Label.fresh();
                Label exit = Label.fresh();
                asmCode.add(Asm.command("sub $t3, $t1, $t0"));
                asmCode.add(Asm.command("bgtz " + ok));
                asmCode.add(Asm.command("li $t2, 0"));
                asmCode.add(Asm.command("j " + exit));
                asmCode.add(Asm.label(ok.toString()));
                asmCode.add(Asm.command("li $t2, 1"));
            }
            case LTE -> {
                Label ok = Label.fresh();
                Label exit = Label.fresh();
                asmCode.add(Asm.command("sub $t3, $t1, $t0"));
                asmCode.add(Asm.command("bgez " + ok));
                asmCode.add(Asm.command("li $t2, 0"));
                asmCode.add(Asm.command("j " + exit));
                asmCode.add(Asm.label(ok.toString()));
                asmCode.add(Asm.command("li $t2, 1"));
            }
            case AND -> asmCode.add(Asm.command("and $t2, $t0, $t1"));
            case OR -> asmCode.add(Asm.command("or $t2, $t0, $t1"));
        }
        asmCode.addAll(Asm.push("$t2"));
        return asmCode;
    }

    @Override
    public List<String> visit(Symbol exp) {
        int size;
        if (exp == Symbol.BYTE_SIZE) size = 1;
        else size = 4;
        List<String> asmCode = ListTools.singleton(Asm.command("li $t0, " + size));
        asmCode.addAll(Asm.push("$t0"));
        return asmCode;
    }
}
package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;

public class MIPSOver implements MIPS {
    private LLVM reference;

    public MIPSOver(LLVM reference) {
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("li $v0, 10\n");
        strb.append("    ");
        strb.append("syscall");
        strb.append("   #");
        reference.print(strb);
    }
}

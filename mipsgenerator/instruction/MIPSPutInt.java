package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSPutInt implements MIPS {
    private Register srcreg;
    private LLVM reference;

    public MIPSPutInt(Register srcreg, LLVM reference) {
        this.srcreg = srcreg;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("move $a0, ");
        strb.append(srcreg.toString());
        strb.append("\n");
        strb.append("    ");
        strb.append("li $v0, 1\n");
        strb.append("    ");
        strb.append("syscall");
        strb.append("   #");
        reference.print(strb);
    }
}

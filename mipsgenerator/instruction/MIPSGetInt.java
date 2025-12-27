package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSGetInt implements MIPS {
    private Register dstreg;
    private LLVM reference;

    public MIPSGetInt(Register dstreg, LLVM reference) {
        this.dstreg = dstreg;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("li $v0, 5\n");
        strb.append("    ");
        strb.append("syscall\n");
        strb.append("    ");
        strb.append("move ");
        strb.append(dstreg.toString());
        strb.append(", $v0");
        strb.append("   #");
        reference.print(strb);
    }
}

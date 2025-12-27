package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSLoadImm implements MIPS {
    private Register dstreg;
    private int imm;
    private LLVM reference;

    public MIPSLoadImm(Register dstreg, int imm, LLVM reference) {
        this.dstreg = dstreg;
        this.imm = imm;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("li ");
        strb.append(dstreg.toString());
        strb.append(", ");
        strb.append(imm);
        strb.append("   #");
        reference.print(strb);
    }
}

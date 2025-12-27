package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSLoadWord implements MIPS {
    private Register dstreg;
    private Register basereg;
    private int offset;
    private LLVM reference;

    public MIPSLoadWord(Register dstreg, Register basereg, int offset, LLVM reference) {
        this.dstreg = dstreg;
        this.basereg = basereg;
        this.offset = offset;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("lw ");
        strb.append(dstreg.toString());
        strb.append(", ");
        strb.append(offset);
        strb.append("(");
        strb.append(basereg.toString());
        strb.append(")");
        strb.append("   #");
        reference.print(strb);
    }
}

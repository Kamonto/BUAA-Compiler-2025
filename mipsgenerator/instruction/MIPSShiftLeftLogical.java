package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSShiftLeftLogical implements MIPS {
    private Register dstreg;
    private Register srcreg;
    private int shift;
    private LLVM reference;

    public MIPSShiftLeftLogical(Register dstreg, Register srcreg, int shift, LLVM reference) {
        this.dstreg = dstreg;
        this.srcreg = srcreg;
        this.shift = shift;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("sll ");
        strb.append(dstreg.toString());
        strb.append(", ");
        strb.append(srcreg.toString());
        strb.append(", ");
        strb.append(shift);
        strb.append("   #");
        reference.print(strb);
    }
}

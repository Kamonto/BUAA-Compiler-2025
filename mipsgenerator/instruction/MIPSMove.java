package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSMove implements MIPS {
    private Register srcreg;
    private Register dstreg;
    private LLVM reference;

    public MIPSMove(Register srcreg, Register dstreg, LLVM reference) {
        this.srcreg = srcreg;
        this.dstreg = dstreg;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        if (srcreg != dstreg) {
            strb.append("    ");
            strb.append("move ");
            strb.append(dstreg.toString());
            strb.append(", ");
            strb.append(srcreg.toString());
            strb.append("   #");
            reference.print(strb);
        }
    }
}

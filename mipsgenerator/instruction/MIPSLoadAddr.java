package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSLoadAddr implements MIPS {
    private Register dstreg;
    private String mipslabel;
    private LLVM reference;

    public MIPSLoadAddr(Register dstreg, String mipslabel, LLVM reference) {
        this.dstreg = dstreg;
        this.mipslabel = mipslabel;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("la ");
        strb.append(dstreg.toString());
        strb.append(", ");
        strb.append(mipslabel);
        strb.append("   #");
        reference.print(strb);
    }
}

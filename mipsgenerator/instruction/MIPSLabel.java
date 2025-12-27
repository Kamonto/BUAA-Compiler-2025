package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;

public class MIPSLabel implements MIPS {
    private String mipslabel;
    private LLVM reference;

    public MIPSLabel(String mipslabel, LLVM reference) {
        this.mipslabel = mipslabel;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append(mipslabel);
        strb.append(":");
        strb.append("   #");
        reference.print(strb);
    }
}

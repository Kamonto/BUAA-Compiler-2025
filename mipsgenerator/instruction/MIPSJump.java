package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;

public class MIPSJump implements MIPS {
    private String mipslabel;
    private LLVM reference;

    public MIPSJump(String mipslabel, LLVM reference) {
        this.mipslabel = mipslabel;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("j ");
        strb.append(mipslabel);
        strb.append("   #");
        reference.print(strb);
    }
}

package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;

public class MIPSJumpAndLink implements MIPS {
    private String mipslabel;
    private LLVM reference;

    public MIPSJumpAndLink(String mipslabel,LLVM reference) {
        this.mipslabel = mipslabel;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("jal ");
        strb.append(mipslabel);
        strb.append("   #");
        reference.print(strb);
    }
}

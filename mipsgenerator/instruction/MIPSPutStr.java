package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;

public class MIPSPutStr implements MIPS {
    private String mipslabel;
    private LLVM reference;

    public MIPSPutStr(String mipslabel, LLVM reference) {
        this.mipslabel = mipslabel;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("la $a0, ");
        strb.append(mipslabel);
        strb.append("\n");
        strb.append("    ");
        strb.append("li $v0, 4\n");
        strb.append("    ");
        strb.append("syscall");
        strb.append("   #");
        reference.print(strb);
    }
}

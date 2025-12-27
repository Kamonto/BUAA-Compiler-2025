package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;

public class MIPSDefGlobalVar implements MIPS {
    private String mipslabel;
    private int value;
    private LLVM reference;

    public MIPSDefGlobalVar(String mipslabel, int value, LLVM reference) {
        this.mipslabel = mipslabel;
        this.value = value;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(mipslabel);
        strb.append(": .word ");
        strb.append(value);
        strb.append("   #");
        reference.print(strb);
    }
}

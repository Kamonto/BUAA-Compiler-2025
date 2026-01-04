package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSBranchIfEqualZero implements MIPS {
    private Register reg1;
    private String mipslabel;
    private LLVM reference;

    public MIPSBranchIfEqualZero(Register reg1, String mipslabel, LLVM reference) {
        this.reg1 = reg1;
        this.mipslabel = mipslabel;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("beqz ");
        strb.append(reg1.toString());
        strb.append(", ");
        strb.append(mipslabel);
        strb.append("   #");
        reference.print(strb);
    }
}

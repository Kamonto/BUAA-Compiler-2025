package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSJumpRegister implements MIPS {
    private Register reg;
    private LLVM reference;

    public MIPSJumpRegister(Register reg, LLVM reference) {
        this.reg = reg;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("jr ");
        strb.append(reg.toString());
        strb.append("   #");
        reference.print(strb);
    }
}

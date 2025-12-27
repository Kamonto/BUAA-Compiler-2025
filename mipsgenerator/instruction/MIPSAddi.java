package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSAddi implements MIPS {
    private Register resreg;
    private Register reg1;
    private int imm;
    private LLVM reference;

    public MIPSAddi(Register resreg, Register reg1, int imm, LLVM reference) {
        this.resreg = resreg;
        this.reg1 = reg1;
        this.imm = imm;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("addi ");
        strb.append(resreg.toString());
        strb.append(", ");
        strb.append(reg1.toString());
        strb.append(", ");
        strb.append(imm);
        strb.append("   #");
        reference.print(strb);
    }
}

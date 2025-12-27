package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSSub implements MIPS {
    private Register resreg;
    private Register reg1;
    private Register reg2;
    private LLVM reference;

    public MIPSSub(Register resreg, Register reg1, Register reg2, LLVM reference) {
        this.resreg = resreg;
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("subu ");
        strb.append(resreg.toString());
        strb.append(", ");
        strb.append(reg1.toString());
        strb.append(", ");
        strb.append(reg2.toString());
        strb.append("   #");
        reference.print(strb);
    }
}

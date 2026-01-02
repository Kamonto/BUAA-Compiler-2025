package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSStoreWord implements MIPS {
    private Register srcreg;
    private Register basereg;
    private String offset;
    private LLVM reference;

    public MIPSStoreWord(Register srcreg, Register basereg, String offset, LLVM reference) {
        this.srcreg = srcreg;
        this.basereg = basereg;
        this.offset = offset;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("sw ");
        strb.append(srcreg.toString());
        strb.append(", ");
        strb.append(offset);
        strb.append("(");
        strb.append(basereg.toString());
        strb.append(")");
        strb.append("   #");
        reference.print(strb);
    }
}

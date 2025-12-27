package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;
import mipsgenerator.Register;

public class MIPSSet implements MIPS {
    private Register resreg;
    private int op;
    private Register reg1;
    private Register reg2;  // 0: seq; 1: sne; 2: slt; 3: sgt; 4: sle; 5: sge;
    private LLVM reference;

    public MIPSSet(Register resreg, int op, Register reg1, Register reg2, LLVM reference) {
        this.resreg = resreg;
        this.op = op;
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        if (op == 0) {
            strb.append("seq ");
        }
        else if (op == 1) {
            strb.append("sne ");
        }
        else if (op == 2) {
            strb.append("slt ");
        }
        else if (op == 3) {
            strb.append("sgt ");
        }
        else if (op == 4) {
            strb.append("sle ");
        }
        else if (op == 5) {
            strb.append("sge ");
        }
        strb.append(resreg.toString());
        strb.append(", ");
        strb.append(reg1.toString());
        strb.append(", ");
        strb.append(reg2.toString());
        strb.append("   #");
        reference.print(strb);
    }
}

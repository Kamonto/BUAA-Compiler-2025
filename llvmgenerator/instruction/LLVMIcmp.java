package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSSet;

public class LLVMIcmp implements LLVM {
    private String reslabel;
    private int op;
    private String label1;
    private String label2;  // 0: eq; 1: ne; 2: slt; 3: sgt; 4: sle; 5: sge;

    public LLVMIcmp(String reslabel, int op, String label1, String label2) {
        this.reslabel = reslabel;
        this.op = op;
        this.label1 = label1;
        this.label2 = label2;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(reslabel);
        strb.append(" = icmp ");
        if (op == 0) {
            strb.append("eq");
        }
        else if (op == 1) {
            strb.append("ne");
        }
        else if (op == 2) {
            strb.append("slt");
        }
        else if (op == 3) {
            strb.append("sgt");
        }
        else if (op == 4) {
            strb.append("sle");
        }
        else if (op == 5) {
            strb.append("sge");
        }
        strb.append(" i32 ");
        strb.append(label1);
        strb.append(", ");
        strb.append(label2);
        strb.append("\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        Register reg1 = mipses.allocRegister(label1);
        mipses.loadLabel(label1, reg1, this);
        Register reg2 = mipses.allocRegister(label2);
        mipses.loadLabel(label2, reg2, this);
        Register resreg = mipses.allocRegister(reslabel);
        MIPSSet mipsSet = new MIPSSet(resreg, op, reg1, reg2, this);
        mipses.addMIPSTextSegment(mipsSet);
        mipses.storeLabel(reslabel, resreg, this);
    }
}

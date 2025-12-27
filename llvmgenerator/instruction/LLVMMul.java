package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSMul;

public class LLVMMul implements LLVM {
    private String reslabel;
    private String label1;
    private String label2;

    public LLVMMul(String reslabel, String label1, String label2) {
        this.reslabel = reslabel;
        this.label1 = label1;
        this.label2 = label2;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(reslabel);
        strb.append(" = mul i32 ");
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
        MIPSMul mipsMul = new MIPSMul(resreg, reg1, reg2, this);
        mipses.addMIPSTextSegment(mipsMul);
        mipses.storeLabel(reslabel, resreg, this);
    }
}

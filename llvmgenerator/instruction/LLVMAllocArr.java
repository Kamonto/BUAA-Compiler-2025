package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSAddi;

public class LLVMAllocArr implements LLVM {
    private String label;
    private int size;

    public LLVMAllocArr(String label, int size) {
        this.label = label;
        this.size = size;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(label);
        strb.append(" = alloca [");
        strb.append(size);
        strb.append(" x i32]\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        int fpoffset = mipses.allocHeapSpace(size);
        Register reg = mipses.allocRegister(label);
        MIPSAddi mipsAddi = new MIPSAddi(reg, Register.$fp, fpoffset, this);
        mipses.addMIPSTextSegment(mipsAddi);
        mipses.storeLabel(label, reg, this);
    }
}

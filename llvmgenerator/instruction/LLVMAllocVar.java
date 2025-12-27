package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSAddi;

public class LLVMAllocVar implements LLVM {
    private String label;
    private boolean isPointer;

    public LLVMAllocVar(String label, boolean isPointer) {
        this.label = label;
        this.isPointer = isPointer;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(label);
        strb.append(" = alloca i32");
        if (isPointer) {
            strb.append("*");
        }
        strb.append("\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        int fpoffset = mipses.allocHeapSpace(1);
        Register reg = mipses.allocRegister(label);
        MIPSAddi mipsAddi = new MIPSAddi(reg, Register.$fp, fpoffset, this);
        mipses.addMIPSTextSegment(mipsAddi);
        mipses.storeLabel(label, reg, this);
    }
}

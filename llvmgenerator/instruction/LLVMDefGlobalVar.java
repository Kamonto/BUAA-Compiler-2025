package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.instruction.MIPSDefGlobalVar;

public class LLVMDefGlobalVar implements LLVM {
    private String label;
    private int value;

    public LLVMDefGlobalVar(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public void print(StringBuilder strb) {
        strb.append(label);
        strb.append(" = dso_local global i32 ");
        strb.append(value);
        strb.append("\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        String mipslabel = label.substring(1);
        MIPSDefGlobalVar mipsDefGlobalVar = new MIPSDefGlobalVar(mipslabel, value, this);
        mipses.addMIPSDataSegment(mipsDefGlobalVar);
    }
}

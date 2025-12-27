package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;

public class LLVMDefFuncEnd implements LLVM {
    public void print(StringBuilder strb) {
        strb.append("}\n");
    }

    public void mipsGenerate(MIPSTable mipses) {

    }
}

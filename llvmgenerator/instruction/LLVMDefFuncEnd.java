package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMDefFuncEnd implements LLVM {
    public void print(StringBuilder strb) {
        strb.append("}\n");
    }
}

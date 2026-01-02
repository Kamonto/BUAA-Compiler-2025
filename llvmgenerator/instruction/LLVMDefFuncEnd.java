package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;

import java.util.HashSet;

public class LLVMDefFuncEnd implements LLVM {
    public void print(StringBuilder strb) {
        strb.append("}\n");
    }

    public void mipsGenerate(MIPSTable mipses) {

    }

    public String getDef() {
        return null;
    }

    public HashSet<String> getUse() {
        HashSet<String> set = new HashSet<String>();
        return set;
    }
}

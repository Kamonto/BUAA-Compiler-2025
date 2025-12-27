package llvmgenerator;

import mipsgenerator.MIPSTable;

public interface LLVM {
    public void print(StringBuilder strb);
    public void mipsGenerate(MIPSTable mipses);
}

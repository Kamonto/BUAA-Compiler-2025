package llvmgenerator;

import mipsgenerator.MIPSTable;

import java.util.HashSet;

public interface LLVM {
    public void print(StringBuilder strb);
    public void mipsGenerate(MIPSTable mipses);
    public String getDef();
    public HashSet<String> getUse();
}

package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;

public class LLVMImport implements LLVM {
    public void print(StringBuilder strb) {
        strb.append("declare i32 @getint()\n" +
                    "declare i32 @getchar()\n" +
                    "declare void @putint(i32)\n" +
                    "declare void @putch(i32)\n" +
                    "declare void @putstr(i8*)\n" +
                    "\n");
    }

    public void mipsGenerate(MIPSTable mipses) {

    }
}

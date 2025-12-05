package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMLabel implements LLVM {
    private int number;

    public LLVMLabel() {
        number = -1;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void print(StringBuilder strb) {
        strb.append(number);
        strb.append(":\n");
    }
}

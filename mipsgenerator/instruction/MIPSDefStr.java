package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;

public class MIPSDefStr implements MIPS {
    private String mipslabel;
    private int size;
    private StringBuilder content;
    private LLVM reference;

    public MIPSDefStr(String mipslabel, int size, StringBuilder content, LLVM reference) {
        this.mipslabel = mipslabel;
        this.size = size;
        this.content = content;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        StringBuilder mipscontent = new StringBuilder();
        for (int i = 0; i < size; i++) {
            char c = content.charAt(i);
            if (c == '\n') {
                mipscontent.append("\\n");
            }
            else if (c == '\0') {
                mipscontent.append("\\0");
            }
            else {
                mipscontent.append(c);
            }
        }
        strb.append(mipslabel);
        strb.append(": .asciiz \"");
        strb.append(mipscontent);
        strb.append("\"");
        strb.append("   #");
        reference.print(strb);
    }
}

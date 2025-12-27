package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.instruction.MIPSDefStr;

public class LLVMDefStr implements LLVM {
    private String label;
    private int size;
    private StringBuilder content;

    public LLVMDefStr(String label, int size, StringBuilder content) {
        this.label = label;
        this.size = size;
        this.content = content;
    }

    public void print(StringBuilder strb) {
        StringBuilder llvmcontent = new StringBuilder();
        for (int i = 0; i < size; i++) {
            char c = content.charAt(i);
            if (c == '\n') {
                llvmcontent.append("\\0A");
            }
            else if (c == '\0') {
                llvmcontent.append("\\00");
            }
            else {
                llvmcontent.append(c);
            }
        }
        strb.append(label);
        strb.append(" = private unnamed_addr constant [");
        strb.append(size);
        strb.append(" x i8] c\"");
        strb.append(llvmcontent);
        strb.append("\"\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        String mipslabel = label.substring(1);
        MIPSDefStr mipsDefStr = new MIPSDefStr(mipslabel, size, content, this);
        mipses.addMIPSDataSegment(mipsDefStr);
    }
}

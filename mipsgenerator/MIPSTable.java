package mipsgenerator;

import llvmgenerator.LLVM;
import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMDefFuncEnd;
import mipsgenerator.instruction.MIPSLoadImm;
import mipsgenerator.instruction.MIPSLoadWord;
import mipsgenerator.instruction.MIPSStoreWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MIPSTable {
    private final LLVMTable llvms;
    private ArrayList<MIPS> mipsDataSegments;
    private ArrayList<MIPS> mipsTextSegments;
    private Optimizer optimizer;
    private HashMap<String, Register> label2regs;
    private int nowfpoffset;
    private HashMap<String, Integer> spoffsets;
    private int nowspoffset;
    private String nowFunc;
    private int callCount;
    private ArrayList<Register> tempPool;
    private int tempPoolindex;

    public MIPSTable(LLVMTable llvms) {
        this.llvms = llvms;
        mipsDataSegments = new ArrayList<MIPS>();
        mipsTextSegments = new ArrayList<MIPS>();
        nowfpoffset = 0;
        spoffsets = new HashMap<String, Integer>();
        nowspoffset = 0;
        nowFunc = null;
        callCount = 0;
        tempPool = new ArrayList<Register>();
        initTempPool();
        tempPoolindex = 0;
    }

    private void initTempPool() {
        tempPool = new ArrayList<Register>(List.of(Register.$t0, Register.$t1, Register.$t2,
                Register.$t3, Register.$t4, Register.$t5, Register.$t6, Register.$t7));
    }

    public int getNowfpoffset() {
        return nowfpoffset;
    }

    public int getNowspoffset() {
        return nowspoffset;
    }

    public String getNowFunc() {
        return nowFunc;
    }

    public int getCallCount() {
        return callCount;
    }

    public void updateCallCount() {
        callCount++;
    }

    public void addMIPSDataSegment(MIPS mips) {
        mipsDataSegments.add(mips);
    }

    public void addMIPSTextSegment(MIPS mips) {
        mipsTextSegments.add(mips);
    }

    public int allocHeapSpace(int size) {
        int oldfpoffset = nowfpoffset;
        nowfpoffset += size * 4;
        return oldfpoffset;
    }

    private void allocStackSpace(String label) {
        nowspoffset -= 4;
        spoffsets.put(label, nowspoffset);
    }

    private int getspoffset(String label) {
        if (spoffsets.containsKey(label)) {
            return spoffsets.get(label);
        }
        return 114514;
    }

    public Register allocRegister(String label) {
        if (label2regs.containsKey(label)) {
            return label2regs.get(label);
        }
        else {
            Register reg = tempPool.get(tempPoolindex);
            tempPoolindex = (tempPoolindex + 1) % tempPool.size();
            return reg;
        }
    }

    public Register allocTempRegister() {
        Register reg = tempPool.get(tempPoolindex);
        tempPoolindex = (tempPoolindex + 1) % tempPool.size();
        return reg;
    }

    public void loadLabel(String label, Register reg, LLVM reference) {
        char c = label.charAt(0);
        if (c == '@') {
            String mipslabel = label.substring(1);
            MIPSLoadWord mipsLoadWord = new MIPSLoadWord(reg, Register.$zero, mipslabel, reference);
            mipsTextSegments.add(mipsLoadWord);
        }
        else if (Character.isDigit(c)) {
            int imm = Integer.parseInt(label);
            MIPSLoadImm mipsLoadImm = new MIPSLoadImm(reg, imm, reference);
            mipsTextSegments.add(mipsLoadImm);
        }
        else {
            if (!label2regs.containsKey(label)) {
                int spoffset = getspoffset(label);
                MIPSLoadWord mipsLoadWord = new MIPSLoadWord(reg, Register.$sp, Integer.toString(spoffset), reference);
                mipsTextSegments.add(mipsLoadWord);
            }
        }
    }

    public void storeLabel(String label, Register reg, LLVM reference) {
        if (!spoffsets.containsKey(label)) {
            allocStackSpace(label);
        }
        char c = label.charAt(0);
        if (c == '@') {
            String mipslabel = label.substring(1);
            MIPSStoreWord mipsStoreWord = new MIPSStoreWord(reg, Register.$zero, mipslabel, reference);
            mipsTextSegments.add(mipsStoreWord);
        }
        else {
            if (!label2regs.containsKey(label)) {
                int spoffset = getspoffset(label);
                MIPSStoreWord mipsStoreWord = new MIPSStoreWord(reg, Register.$sp, Integer.toString(spoffset), reference);
                mipsTextSegments.add(mipsStoreWord);
            }
        }
    }

    public void newFunc(String funcLabel, ArrayList<String> paramLabels, LLVM reference) {
        nowFunc = funcLabel.substring(1);
        spoffsets.clear();
        nowfpoffset = 0;
        nowspoffset = 0;
        optimizer = new Optimizer(funcllvms(llvms.getMergedllvms(), reference));
        optimizer.cutBlocks();
        optimizer.defuse();
        optimizer.allocreg();
        label2regs = optimizer.getLabel2regs();
        for (String paramLabel : paramLabels) {
            allocStackSpace(paramLabel);
        }
        for (String paramLabel : paramLabels) {
            if (label2regs.containsKey(paramLabel)) {
                Register reg = label2regs.get(paramLabel);
                int spoffset = getspoffset(paramLabel);
                MIPSLoadWord mipsLoadWord = new MIPSLoadWord(reg, Register.$sp, Integer.toString(spoffset), reference);
                mipsTextSegments.add(mipsLoadWord);
            }
        }
    }

    public ArrayList<Register> getActiveRegsWhenCall(LLVM reference) {
        ArrayList<Register> activeRegsWhenCall = new ArrayList<Register>();
        ArrayList<String> activeLabelsWhenCall = optimizer.getActiveLabelsWhenCall(reference);
        for (String label : activeLabelsWhenCall) {
            if (label2regs.containsKey(label)) {
                activeRegsWhenCall.add(label2regs.get(label));
            }
        }
        return activeRegsWhenCall;
    }

    public StringBuilder print() {
        StringBuilder strb = new StringBuilder();
        strb.append(".data\n");
        for (MIPS mips : mipsDataSegments) {
            mips.print(strb);
        }
        strb.append("    align: .align 2\n");
        strb.append("\n.text\n");
        strb.append("    la $fp, align\n");
        strb.append("    j main\n");
        for (MIPS mips : mipsTextSegments) {
            mips.print(strb);
        }
        return strb;
    }

    private ArrayList<LLVM> funcllvms(ArrayList<LLVM> llvms, LLVM reference) {
        ArrayList<LLVM> funcllvms = new ArrayList<LLVM>();
        int index = llvms.indexOf(reference);
        while (true) {
            if (llvms.get(index) instanceof LLVMDefFuncEnd) {
                break;
            }
            funcllvms.add(llvms.get(index));
            index++;
        }
        return funcllvms;
    }
}

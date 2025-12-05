package llvmgenerator;

import llvmgenerator.instruction.*;

import java.util.ArrayList;

public class LLVMTable {
    private ArrayList<LLVM> llvms;
    private ArrayList<LLVM> llvmDefStrs;
    private ArrayList<LLVM> llvmDefStatics;

    public LLVMTable() {
        llvms = new ArrayList<LLVM>();
        llvmDefStrs = new ArrayList<LLVM>();
        llvmDefStatics = new ArrayList<LLVM>();
    }

    public void addLLVM(LLVM llvm) {
        llvms.add(llvm);
    }

    public void addLLVMDefStr(LLVM llvm) {
        llvmDefStrs.add(llvm);
    }

    public void addLLVMDefStatics(LLVM llvm) {
        llvmDefStatics.add(llvm);
    }

    public int llvmDefStaticsSize() {
        return llvmDefStatics.size();
    }

    public int llvmDefStrsSize() {
        return llvmDefStrs.size();
    }

    public void checkLastLabel() {
        int size = llvms.size();
        if (llvms.get(size - 1) instanceof LLVMDefFuncEnd && llvms.get(size - 2) instanceof LLVMLabel) {
            llvms.remove(size - 2);
        }
        size = llvms.size();
        if (llvms.get(size - 1) instanceof LLVMDefFuncEnd && !(llvms.get(size - 2) instanceof LLVMRet)) {
            LLVMRet llvmRet = new LLVMRet(false, null);
            llvms.add(size - 1, llvmRet);
        }
    }

    public StringBuilder print() {
        StringBuilder strb = new StringBuilder();
        ArrayList<LLVM> mergedllvms = mergeLLVM();
        for (LLVM llvm : mergedllvms) {
            llvm.print(strb);
        }
        return strb;
    }

    private ArrayList<LLVM> mergeLLVM() {
        ArrayList<LLVM> mergedllvms = new ArrayList<LLVM>();
        boolean flag = false;
        for (LLVM llvm : llvms) {
            if (flag == false && !(llvm instanceof LLVMImport || llvm instanceof LLVMDefGlobalVar || llvm instanceof LLVMDefGlobalArr)) {
                for (LLVM llvmDefStatic : llvmDefStatics) {
                    mergedllvms.add(llvmDefStatic);
                }
                for (LLVM llvmDefStr : llvmDefStrs) {
                    mergedllvms.add(llvmDefStr);
                }
                flag = true;
            }
            mergedllvms.add(llvm);
        }
        return mergedllvms;
    }
}

package symbolizer;

import llvmgenerator.instruction.LLVMLabel;

import java.util.ArrayList;

public class Scope {
    private ArrayList<Integer> scopeStack;
    private int nowMaxScope;
    private int loopLayer;
    private ArrayList<LLVMLabel> loopEndLabelStack;
    private ArrayList<LLVMLabel> endLabelStack;
    private int nowMaxNumber;

    public Scope() {
        scopeStack = new ArrayList<Integer>();
        scopeStack.add(1);
        nowMaxScope = 1;
        loopLayer = 0;
        loopEndLabelStack = new ArrayList<LLVMLabel>();
        endLabelStack = new ArrayList<LLVMLabel>();
        nowMaxNumber = 0;
    }

    public ArrayList<Integer> getScopeStack() {
        return scopeStack;
    }

    public int getScope() {
        return scopeStack.get(scopeStack.size() - 1);
    }

    public int getLayer() {
        return scopeStack.size();
    }

    public int getNowMaxScope() {
        return nowMaxScope;
    }

    public int getLoopLayer() {
        return loopLayer;
    }

    public int getNowMaxNumber() {
        return nowMaxNumber;
    }

    public void push() {
        nowMaxScope++;
        scopeStack.add(nowMaxScope);
    }

    public void pop() {
        scopeStack.remove(scopeStack.size() - 1);
    }

    public void forcePush() {
        nowMaxScope++;
        scopeStack.add(nowMaxScope);
        nowMaxNumber = 0;
    }

    public void skipLabel() {
        nowMaxNumber++;
    }

    public void forcePop() {
        scopeStack.remove(scopeStack.size() - 1);
        nowMaxScope--;
    }

    public void entryLoop() {
        loopLayer++;
    }

    public void exitLoop() {
        loopLayer--;
    }

    public void loopEndLabelStackPush(LLVMLabel loopEndLabel) {
        loopEndLabelStack.add(loopEndLabel);
    }

    public void loopEndLabelStackPop() {
        loopEndLabelStack.remove(loopEndLabelStack.size() - 1);
    }

    public LLVMLabel getLoopEndLabel() {
        return loopEndLabelStack.get(loopEndLabelStack.size() - 1);
    }

    public void endLabelStackPush(LLVMLabel endLabel) {
        endLabelStack.add(endLabel);
    }

    public void endLabelStackPop() {
        endLabelStack.remove(endLabelStack.size() - 1);
    }

    public LLVMLabel getEndLabel() {
        return endLabelStack.get(endLabelStack.size() - 1);
    }

    public int allocNumber() {
        int res = nowMaxNumber;
        nowMaxNumber++;
        return res;
    }
}

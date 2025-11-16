package symbolizer;

import java.util.ArrayList;

public class Scope {
    private ArrayList<Integer> scopeStack;
    private int nowMaxScope;
    private int loopLayer;

    public Scope() {
        scopeStack = new ArrayList<Integer>();
        scopeStack.add(1);
        nowMaxScope = 1;
        loopLayer = 0;
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

    public void push() {
        nowMaxScope++;
        scopeStack.add(nowMaxScope);
    }

    public void pop() {
        scopeStack.remove(scopeStack.size() - 1);
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
}

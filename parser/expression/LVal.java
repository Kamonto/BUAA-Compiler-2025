package parser.expression;

import lexer.Token;
import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMGetElementArr;
import llvmgenerator.instruction.LLVMGetElementPtr;
import llvmgenerator.instruction.LLVMLoad;
import symbolizer.*;

import java.util.ArrayList;

public class LVal {
    private Token ident;
    private boolean isArray;
    private Exp exp;

    public LVal(Token ident, boolean isArray, Exp exp) {
        this.ident = ident;
        this.isArray = isArray;
        this.exp = exp;
    }

    public void print(StringBuilder strb) {
        strb.append("IDENFR ").append(ident.getContent()).append("\n");
        if (isArray) {
            strb.append("LBRACK [\n");
            exp.print(strb);
            strb.append("RBRACK ]\n");
        }
        strb.append("<LVal>\n");
    }

    public void symbolize(boolean isAssign, SymbolTable symbols, Scope scope) {
        symbols.checkUndeclaredVariable(ident);
        if (isAssign) {
            symbols.checkAssignToConst(ident);
        }
        if (isArray) {
            exp.symbolize(symbols, scope);
        }
    }

    public boolean isArray(SymbolTable symbols, Scope scope) {
        if (isArray) {
            return false;
        }
        else {
            return symbols.isArray(scope, ident);
        }
    }

    public int calculate(SymbolTable symbols, Scope scope) {
        ConstSymbol constSymbol;
        int value = 0;
        ArrayList<Integer> scopeStack = scope.getScopeStack();
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            constSymbol = symbols.findConstSymbol(scopeStack.get(i), ident.getContent());
            if (constSymbol != null) {
                if (isArray) {
                    value = constSymbol.getValue(exp.calculate(symbols, scope));
                }
                else {
                    value = constSymbol.getValue();
                }
                break;
            }
        }
        return value;
    }

    public String llvmGenerate(boolean evaluate, SymbolTable symbols, Scope scope, LLVMTable llvms) {
        Symbol symbol = null;
        String label = null;
        ArrayList<Integer> scopeStack = scope.getScopeStack();
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            symbol = symbols.findSymbol(scopeStack.get(i), ident.getContent());
            if (symbol != null) {
                label = symbol.getLabel();
                break;
            }
        }
        ArrSymbol arrSymbol;
        int size = -2;   // -2: not array; -1: ptr array; >0: real array
        if (symbol != null) {
            arrSymbol = symbols.findArrSymbol(symbol.getScope(), ident.getContent());
            if (arrSymbol != null) {
                size = arrSymbol.getSize();
            }
        }
        if (isArray) {
            String offset = exp.llvmGenerate(symbols, scope, llvms);
            String ptrlabel = "%" + scope.allocNumber();
            if (size == -1) {
                LLVMGetElementPtr llvmGetElementPtr = new LLVMGetElementPtr(ptrlabel, label, offset);
                llvms.addLLVM(llvmGetElementPtr);
            }
            else {
                LLVMGetElementArr llvmGetElementArr = new LLVMGetElementArr(ptrlabel, size, label, offset);
                llvms.addLLVM(llvmGetElementArr);
            }
            if (evaluate) {
                String reslabel = "%" + scope.allocNumber();
                LLVMLoad llvmLoad = new LLVMLoad(ptrlabel, reslabel, false);
                llvms.addLLVM(llvmLoad);
                return reslabel;
            }
            else {
                return ptrlabel;
            }
        }
        else {
            if (evaluate) {
                if (size == -2) {
                    String reslabel = "%" + scope.allocNumber();
                    LLVMLoad llvmLoad = new LLVMLoad(label, reslabel, false);
                    llvms.addLLVM(llvmLoad);
                    return reslabel;
                }
                else if (size == -1) {
                    return label;
                }
                else {
                    String reslabel = "%" + scope.allocNumber();
                    LLVMGetElementArr llvmGetElementArr = new LLVMGetElementArr(reslabel, size, label, "0");
                    llvms.addLLVM(llvmGetElementArr);
                    return reslabel;
                }
            }
            else {
                return label;
            }
        }
    }
}

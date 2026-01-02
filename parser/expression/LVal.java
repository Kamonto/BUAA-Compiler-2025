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

    public boolean getIsArray() {
        return isArray;
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
        if (isArray) {
            String offset = exp.llvmGenerate(symbols, scope, llvms);
            String ptrlabel;
            if (!offset.matches("-?\\d+") || Integer.parseInt(offset) != 0) {
                ptrlabel = "%" + scope.allocNumber();
                LLVMGetElementPtr llvmGetElementPtr = new LLVMGetElementPtr(ptrlabel, label, offset);
                llvms.addLLVM(llvmGetElementPtr);
            }
            else {
                ptrlabel = label;
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
            return label;
        }
    }
}

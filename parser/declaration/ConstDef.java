package parser.declaration;

import lexer.Token;
import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.*;
import parser.expression.ConstExp;
import parser.type.BType;
import symbolizer.*;

import java.util.ArrayList;

public class ConstDef {
    private Token ident;
    private boolean isArray;
    private ConstExp constExp;
    private ConstInitVal constInitVal;

    public ConstDef(Token ident, boolean isArray, ConstExp constExp, ConstInitVal constInitVal) {
        this.ident = ident;
        this.isArray = isArray;
        this.constExp = constExp;
        this.constInitVal = constInitVal;
    }

    public void print(StringBuilder strb) {
        strb.append("IDENFR ").append(ident.getContent()).append("\n");
        if (isArray) {
            strb.append("LBRACK [\n");
            constExp.print(strb);
            strb.append("RBRACK ]\n");
        }
        strb.append("ASSIGN =\n");
        constInitVal.print(strb);
        strb.append("<ConstDef>\n");
    }

    public void symbolize(BType bType, SymbolTable symbols, Scope scope) {
        symbols.checkDuplicateDeclaration(ident);
        Symbol symbol;
        if (isArray) {
            symbol = new Symbol(scope, ident.getContent(), SymbolType.ConstIntArray);
        }
        else {
            symbol = new Symbol(scope, ident.getContent(), SymbolType.ConstInt);
        }
        symbols.addSymbol(symbol);
    }

    public void llvmGenerate(BType bType, SymbolTable symbols, Scope scope, LLVMTable llvms) {
        if (scope.getLayer() == 1) {
            String label = "@" + ident.getContent();
            Symbol symbol = symbols.getSymbol(scope.getScope(), ident.getContent());
            symbol.setLabel(label);
            if (isArray) {
                int size = constExp.calculate(symbols, scope);
                ArrSymbol arrSymbol = new ArrSymbol(scope, ident.getContent(), size);
                arrSymbol.setLabel(label);
                symbols.addArrSymbol(arrSymbol);
                ArrayList<Integer> values = constInitVal.calculate(symbols, scope, size);
                ConstSymbol constSymbol = new ConstSymbol(scope, ident.getContent(), values);
                constSymbol.setLabel(label);
                symbols.addConstSymbol(constSymbol);
                LLVMDefGlobalArr llvmDefGlobalArr = new LLVMDefGlobalArr(label, size, values);
                llvms.addLLVM(llvmDefGlobalArr);
            }
            else {
                int value = constInitVal.calculate(symbols, scope, 1).get(0);
                ConstSymbol constSymbol = new ConstSymbol(scope, ident.getContent(), value);
                constSymbol.setLabel(label);
                symbols.addConstSymbol(constSymbol);
                LLVMDefGlobalVar llvmDefGlobalVar = new LLVMDefGlobalVar(label, value);
                llvms.addLLVM(llvmDefGlobalVar);
            }
        }
        else {
            String label = "%" + scope.allocNumber();
            Symbol symbol = symbols.getSymbol(scope.getScope(), ident.getContent());
            symbol.setLabel(label);
            if (isArray) {
                int size = constExp.calculate(symbols, scope);
                ArrSymbol arrSymbol = new ArrSymbol(scope, ident.getContent(), size);
                arrSymbol.setLabel(label);
                symbols.addArrSymbol(arrSymbol);
                ArrayList<Integer> values = constInitVal.calculate(symbols, scope, size);
                ConstSymbol constSymbol = new ConstSymbol(scope, ident.getContent(), values);
                constSymbol.setLabel(label);
                symbols.addConstSymbol(constSymbol);
                LLVMAllocArr llvmAllocArr = new LLVMAllocArr(label, size);
                llvms.addLLVM(llvmAllocArr);
                String templabel = null;
                for (int i = 0; i < size; i++) {
                    String reslabel;
                    if (i == 0) {
                        reslabel = label;
                    }
                    else {
                        reslabel = "%" + scope.allocNumber();
                        LLVMGetElementPtr llvmGetElementPtr = new LLVMGetElementPtr(reslabel, templabel, "1");
                        llvms.addLLVM(llvmGetElementPtr);
                    }
                    templabel = reslabel;
                    LLVMStore llvmStore = new LLVMStore(Integer.toString(values.get(i)), reslabel, false);
                    llvms.addLLVM(llvmStore);
                }
            }
            else {
                int value = constInitVal.calculate(symbols, scope, 1).get(0);
                ConstSymbol constSymbol = new ConstSymbol(scope, ident.getContent(), value);
                constSymbol.setLabel(label);
                symbols.addConstSymbol(constSymbol);
                LLVMMove llvmMove = new LLVMMove(Integer.toString(value), label);
                llvms.addLLVM(llvmMove);
            }
        }
    }
}

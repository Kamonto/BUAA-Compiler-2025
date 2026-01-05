package parser.declaration;

import lexer.Token;
import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.*;
import parser.expression.ConstExp;
import parser.type.BType;
import symbolizer.*;

import java.util.ArrayList;

public class VarDef {
    private Token ident;
    private boolean isArray;
    private ConstExp constExp;
    private boolean hasInitValue;
    private InitVal initVal;

    public VarDef(Token ident, boolean isArray, ConstExp constExp, boolean hasInitValue, InitVal initVal) {
        this.ident = ident;
        this.isArray = isArray;
        this.constExp = constExp;
        this.hasInitValue = hasInitValue;
        this.initVal = initVal;
    }

    public void print(StringBuilder strb) {
        strb.append("IDENFR ").append(ident.getContent()).append("\n");
        if (isArray) {
            strb.append("LBRACK [\n");
            constExp.print(strb);
            strb.append("RBRACK ]\n");
        }
        if (hasInitValue) {
            strb.append("ASSIGN =\n");
            initVal.print(strb);
        }
        strb.append("<VarDef>\n");
    }

    public void symbolize(boolean isStatic, BType bType, SymbolTable symbols, Scope scope) {
        symbols.checkDuplicateDeclaration(ident);
        Symbol symbol;
        if (isStatic) {
            if (isArray) {
                symbol = new Symbol(scope, ident.getContent(), SymbolType.StaticIntArray);
            }
            else {
                symbol = new Symbol(scope, ident.getContent(), SymbolType.StaticInt);
            }
        }
        else {
            if (isArray) {
                symbol = new Symbol(scope, ident.getContent(), SymbolType.IntArray);
            }
            else {
                symbol = new Symbol(scope, ident.getContent(), SymbolType.Int);
            }
        }
        symbols.addSymbol(symbol);
    }

    public void llvmGenerate(boolean isStatic, BType bType, SymbolTable symbols, Scope scope, LLVMTable llvms) {
        if (isStatic) {
            Symbol symbol = symbols.getSymbol(scope.getScope(), ident.getContent());
            if (symbol.getLabel() == null) {
                String label = "@" + ident.getContent() + ".static" + llvms.llvmDefStaticsSize();
                symbol.setLabel(label);
                if (isArray) {
                    int size = constExp.calculate(symbols, scope);
                    ArrSymbol arrSymbol = new ArrSymbol(scope, ident.getContent(), size);
                    arrSymbol.setLabel(label);
                    symbols.addArrSymbol(arrSymbol);
                    ArrayList<Integer> values;
                    if (hasInitValue) {
                        values = initVal.calculate(symbols, scope, size);
                    }
                    else {
                        values = new ArrayList<Integer>();
                        for (int i = 0; i < size; i++) {
                            values.add(0);
                        }
                    }
                    LLVMDefGlobalArr llvmDefGlobalArr = new LLVMDefGlobalArr(label, size, values);
                    llvms.addLLVMDefStatics(llvmDefGlobalArr);
                }
                else {
                    int value;
                    if (hasInitValue) {
                        value = initVal.calculate(symbols, scope, 1).get(0);
                    }
                    else {
                        value = 0;
                    }
                    LLVMDefGlobalVar llvmDefGlobalVar = new LLVMDefGlobalVar(label, value);
                    llvms.addLLVMDefStatics(llvmDefGlobalVar);
                }
            }
        }
        else if (scope.getLayer() == 1) {
            String label = "@" + ident.getContent();
            Symbol symbol = symbols.getSymbol(scope.getScope(), ident.getContent());
            symbol.setLabel(label);
            if (isArray) {
                int size = constExp.calculate(symbols, scope);
                ArrSymbol arrSymbol = new ArrSymbol(scope, ident.getContent(), size);
                arrSymbol.setLabel(label);
                symbols.addArrSymbol(arrSymbol);
                ArrayList<Integer> values;
                if (hasInitValue) {
                    values = initVal.calculate(symbols, scope, size);
                }
                else {
                    values = new ArrayList<Integer>();
                    for (int i = 0; i < size; i++) {
                        values.add(0);
                    }
                }
                LLVMDefGlobalArr llvmDefGlobalArr = new LLVMDefGlobalArr(label, size, values);
                llvms.addLLVM(llvmDefGlobalArr);
            }
            else {
                int value;
                if (hasInitValue) {
                    value = initVal.calculate(symbols, scope, 1).get(0);
                }
                else {
                    value = 0;
                }
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
                LLVMAllocArr llvmAllocArr = new LLVMAllocArr(label, size);
                llvms.addLLVM(llvmAllocArr);
                if (hasInitValue) {
                    ArrayList<String> initlabels = initVal.llvmGenerate(symbols, scope, llvms);
                    for (int i = initlabels.size(); i < size; i++) {
                        initlabels.add("0");
                    }
                    int initsize = initlabels.size();
                    for (int i = 0; i < initsize; i++) {
                        if (!initlabels.get(i).equals("0")) {
                            String reslabel;
                            if (i == 0) {
                                reslabel = label;
                            }
                            else {
                                reslabel = "%" + scope.allocNumber();
                                LLVMGetElementPtr llvmGetElementPtr = new LLVMGetElementPtr(reslabel, label, Integer.toString(i));
                                llvms.addLLVM(llvmGetElementPtr);
                            }
                            LLVMStore llvmStore = new LLVMStore(initlabels.get(i), reslabel, false);
                            llvms.addLLVM(llvmStore);
                        }
                    }
                }
            }
            else {
                if (hasInitValue) {
                    String initlabel = initVal.llvmGenerate(symbols, scope, llvms).get(0);
                    LLVMMove llvmMove = new LLVMMove(initlabel, label);
                    llvms.addLLVM(llvmMove);
                }
            }
        }
    }
}

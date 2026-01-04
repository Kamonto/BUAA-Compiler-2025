package parser.expression;

import lexer.Token;
import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.*;
import symbolizer.*;

import java.util.ArrayList;

public class UnaryExp {
    private ArrayList<UnaryOp> unaryOps;
    private boolean isPrimaryExp;
    private PrimaryExp primaryExp;
    private Token ident;
    private boolean hasFuncRParams;
    private FuncRParams funcRParams;

    public UnaryExp(ArrayList<UnaryOp> unaryOps, boolean isPrimaryExp, PrimaryExp primaryExp,
                    Token ident, boolean hasFuncRParams, FuncRParams funcRParams) {
        this.unaryOps = unaryOps;
        this.isPrimaryExp = isPrimaryExp;
        this.primaryExp = primaryExp;
        this.ident = ident;
        this.hasFuncRParams = hasFuncRParams;
        this.funcRParams = funcRParams;
    }

    public void print(StringBuilder strb) {
        for (UnaryOp unaryOp : unaryOps) {
            unaryOp.print(strb);
        }
        if (isPrimaryExp) {
            primaryExp.print(strb);
        }
        else {
            strb.append("IDENFR ").append(ident.getContent()).append("\n");
            strb.append("LPARENT (\n");
            if (hasFuncRParams) {
                funcRParams.print(strb);
            }
            strb.append("RPARENT )\n");
        }
        strb.append("<UnaryExp>\n");
        for (UnaryOp unaryOp : unaryOps) {
            strb.append("<UnaryExp>\n");
        }
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        if (isPrimaryExp) {
            primaryExp.symbolize(symbols, scope);
        }
        else {
            symbols.checkUndeclaredVariable(ident);
            FuncSymbol funcSymbol = new FuncSymbol(scope, ident.getContent(), false);
            if (hasFuncRParams) {
                funcRParams.symbolize(funcSymbol, symbols, scope);
            }
            symbols.checkFuncParamsMismatch(ident, funcSymbol);
        }
    }

    public boolean isArray(SymbolTable symbols, Scope scope) {
        if (isPrimaryExp) {
            return primaryExp.isArray(symbols, scope);
        }
        else {
            return false;
        }
    }

    public int calculate(SymbolTable symbols, Scope scope) {
        int value = 0;
        int op = 1;
        int cnt = 0;
        for (UnaryOp unaryOp : unaryOps) {
            int type = unaryOp.getType();
            op *= type;
            if (type == 0) {
                cnt++;
            }
        }
        if (isPrimaryExp) {
            value = primaryExp.calculate(symbols, scope);
        }
        else {
            // TODO: idk maybe illegal
        }
        if (op == 0) {
            if (cnt % 2 == 1) {
                value = (value == 0) ? 1 : 0;
            }
            else {
                value = (value == 0) ? 0 : 1;
            }
        }
        else {
            value *= op;
        }
        return value;
    }

    public String llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        int op = 1;
        int cnt = 0;
        for (UnaryOp unaryOp : unaryOps) {
            int type = unaryOp.getType();
            op *= type;
            if (type == 0) {
                cnt++;
            }
        }
        String reslabel;
        if (isPrimaryExp) {
            reslabel = primaryExp.llvmGenerate(symbols, scope, llvms);
        }
        else {
            if (ident.getContent().equals("getint")) {
                reslabel = "%" + scope.allocNumber();
                LLVMGetInt llvmGetInt = new LLVMGetInt(reslabel);
                llvms.addLLVM(llvmGetInt);
            }
            else {
                Symbol symbol = symbols.findSymbol(1, ident.getContent());
                String label = symbol.getLabel();
                FuncSymbol funcSymbol = symbols.findFuncSymbol(1, ident.getContent());
                boolean hasReturnValue = funcSymbol.hasReturnValue();
                ArrayList<String> paramLabels = new ArrayList<String>();
                ArrayList<Boolean> isPointers = funcSymbol.getParams();
                if (hasFuncRParams) {
                    funcRParams.llvmGenerate(paramLabels, symbols, scope, llvms);
                }
                if (hasReturnValue) {
                    reslabel = "%" + scope.allocNumber();
                }
                else {
                    reslabel = null;
                }
                LLVMCall llvmCall = new LLVMCall(reslabel, label, hasReturnValue, paramLabels, isPointers);
                llvms.addLLVM(llvmCall);
            }
        }
        if (reslabel != null && op == -1) {
            String oldlabel = reslabel;
            if (oldlabel.matches("-?\\d+")) {
                int res = -Integer.parseInt(oldlabel);
                reslabel = Integer.toString(res);
            }
            else {
                reslabel = "%" + scope.allocNumber();
                LLVMSub llvmSub = new LLVMSub(reslabel, "0", oldlabel);
                llvms.addLLVM(llvmSub);
            }
        }
        if (reslabel != null && op == 0) {
            String oldlabel = reslabel;
            reslabel = "%" + scope.allocNumber();
            if (cnt % 2 == 1) {
                LLVMIcmp llvmIcmp = new LLVMIcmp(reslabel, 0, oldlabel, "0");
                llvms.addLLVM(llvmIcmp);
            }
            else {
                LLVMIcmp llvmIcmp = new LLVMIcmp(reslabel, 1, oldlabel, "0");
                llvms.addLLVM(llvmIcmp);
            }
        }
        return reslabel;
    }
}

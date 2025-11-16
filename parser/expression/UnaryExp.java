package parser.expression;

import lexer.Token;
import symbolizer.FuncSymbol;
import symbolizer.Scope;
import symbolizer.SymbolTable;

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
}

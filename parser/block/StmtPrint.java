package parser.block;

import lexer.Token;
import parser.expression.Exp;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class StmtPrint implements Stmt {
    private Token printfToken;
    private String stringConst;
    private ArrayList<Exp> exps;

    public StmtPrint(Token printfToken, String stringConst, ArrayList<Exp> exps) {
        this.printfToken = printfToken;
        this.stringConst = stringConst;
        this.exps = exps;
    }

    public void print(StringBuilder strb) {
        strb.append("PRINTFTK printf\n");
        strb.append("LPARENT (\n");
        strb.append("STRCON ").append(stringConst).append("\n");
        for (Exp exp : exps) {
            strb.append("COMMA ,\n");
            exp.print(strb);
        }
        strb.append("RPARENT )\n");
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        symbols.checkFormatCharInPrintfMismatch(stringConst, exps.size(), printfToken);
        for (Exp exp : exps) {
            exp.symbolize(symbols, scope);
        }
    }
}

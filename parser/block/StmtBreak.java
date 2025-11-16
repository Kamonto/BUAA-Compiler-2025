package parser.block;

import lexer.Token;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtBreak implements Stmt {
    private Token breakToken;

    public StmtBreak(Token breakToken) {
        this.breakToken = breakToken;
    }

    public void print(StringBuilder strb) {
        strb.append("BREAKTK break\n");
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        symbols.checkContinueOrBreakOutOfLoop(breakToken);
    }
}

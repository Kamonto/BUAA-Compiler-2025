package parser.block;

import lexer.Token;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtContinue implements Stmt {
    private Token continueToken;

    public StmtContinue(Token continueToken) {
        this.continueToken = continueToken;
    }

    public void print(StringBuilder strb) {
        strb.append("CONTINUETK continue\n");
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        symbols.checkContinueOrBreakOutOfLoop(continueToken);
    }
}

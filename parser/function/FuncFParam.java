package parser.function;

import lexer.Token;
import parser.type.BType;
import symbolizer.*;

public class FuncFParam {
    private BType bType;
    private Token ident;
    private boolean isArray;

    public FuncFParam(BType bType, Token ident, boolean isArray) {
        this.bType = bType;
        this.ident = ident;
        this.isArray = isArray;
    }

    public void print(StringBuilder strb) {
        bType.print(strb);
        strb.append("IDENFR ").append(ident.getContent()).append("\n");
        if (isArray) {
            strb.append("LBRACK [\n");
            strb.append("RBRACK ]\n");
        }
        strb.append("<FuncFParam>\n");
    }

    public void symbolize(FuncSymbol funcSymbol, SymbolTable symbols, Scope scope) {
        symbols.checkDuplicateDeclaration(ident);
        Symbol symbol;
        if (isArray) {
            symbol = new Symbol(scope, ident.getContent(), SymbolType.IntArray);
            funcSymbol.addParam(true);
        }
        else {
            symbol = new Symbol(scope, ident.getContent(), SymbolType.Int);
            funcSymbol.addParam(false);
        }
        symbols.addSymbol(symbol);
    }
}

package parser.declaration;

import lexer.Token;
import parser.expression.ConstExp;
import parser.type.BType;
import symbolizer.Scope;
import symbolizer.Symbol;
import symbolizer.SymbolTable;
import symbolizer.SymbolType;

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
}

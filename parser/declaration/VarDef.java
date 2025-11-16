package parser.declaration;

import lexer.Token;
import parser.expression.ConstExp;
import parser.type.BType;
import symbolizer.Scope;
import symbolizer.Symbol;
import symbolizer.SymbolTable;
import symbolizer.SymbolType;

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
}

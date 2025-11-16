package parser.declaration;

import parser.type.BType;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class ConstDecl implements Decl {
    private BType bType;
    private ArrayList<ConstDef> constDefs;

    public ConstDecl(BType bType, ArrayList<ConstDef> constDefs) {
        this.bType = bType;
        this.constDefs = constDefs;
    }

    public void print(StringBuilder strb) {
        strb.append("CONSTTK const\n");
        bType.print(strb);
        int size = constDefs.size();
        if (size > 0) {
            constDefs.get(0).print(strb);
            for (int i = 1; i < size; i++) {
                strb.append("COMMA ,\n");
                constDefs.get(i).print(strb);
            }
        }
        strb.append("SEMICN ;\n");
        strb.append("<ConstDecl>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        for (ConstDef constDef : constDefs) {
            constDef.symbolize(bType, symbols, scope);
        }
    }
}

package parser.type;

import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class FuncType {
    private boolean isVoid;

    public FuncType(boolean isVoid) {
        this.isVoid = isVoid;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public void print(StringBuilder strb) {
        if (isVoid) {
            strb.append("VOIDTK void\n");
        }
        else {
            strb.append("INTTK int\n");
        }
        strb.append("<FuncType>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {

    }
}

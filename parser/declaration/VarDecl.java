package parser.declaration;

import parser.type.BType;

import java.util.ArrayList;

public class VarDecl implements Decl {
    private boolean isStatic;
    private BType bType;
    private ArrayList<VarDef> varDefs;

    public VarDecl(boolean isStatic, BType bType, ArrayList<VarDef> varDefs) {
        this.isStatic = isStatic;
        this.bType = bType;
        this.varDefs = varDefs;
    }

    public void print(StringBuilder strb) {
        if (isStatic) {
            strb.append("STATICTK static\n");
        }
        bType.print(strb);
        int size = varDefs.size();
        if (size > 0) {
            varDefs.get(0).print(strb);
            for (int i = 1; i < size; i++) {
                strb.append("COMMA ,\n");
                varDefs.get(i).print(strb);
            }
        }
        strb.append("SEMICN ;\n");
        strb.append("<VarDecl>\n");
    }
}

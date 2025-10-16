package parser.declaration;

import parser.expression.ConstExp;

import java.util.ArrayList;

public class ConstInitVal {
    private boolean isArray;
    private ConstExp constExp;
    private ArrayList<ConstExp> constExps;

    public ConstInitVal(boolean isArray, ConstExp constExp, ArrayList<ConstExp> constExps) {
        this.isArray = isArray;
        this.constExp = constExp;
        this.constExps = constExps;
    }

    public void print(StringBuilder strb) {
        if (isArray) {
            strb.append("LBRACE {\n");
            int size = constExps.size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    strb.append("COMMA ,\n");
                }
                constExps.get(i).print(strb);
            }
            strb.append("RBRACE }\n");
        }
        else {
            constExp.print(strb);
        }
        strb.append("<ConstInitVal>\n");
    }
}

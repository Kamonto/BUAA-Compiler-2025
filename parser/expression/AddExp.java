package parser.expression;

import java.util.ArrayList;

public class AddExp {
    private ArrayList<MulExp> mulExps;
    private ArrayList<Integer> opTypes; // 0: first, 1: +, 2: -

    public AddExp(ArrayList<MulExp> mulExps, ArrayList<Integer> opTypes) {
        this.mulExps = mulExps;
        this.opTypes = opTypes;
    }

    public void print(StringBuilder strb) {
        int size = mulExps.size();
        for (int i = 0; i < size; i++) {
            int opType = opTypes.get(i);
            if (opType == 1) {
                strb.append("PLUS +\n");
            } else if (opType == 2) {
                strb.append("MINU -\n");
            }
            mulExps.get(i).print(strb);
            strb.append("<AddExp>\n");
        }
    }
}

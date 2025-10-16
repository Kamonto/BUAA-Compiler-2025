package parser.expression;

import java.util.ArrayList;

public class RelExp {
    private ArrayList<AddExp> addExps;
    private ArrayList<Integer> opTypes; // 0: first, 1: <, 2: >, 3: <=, 4: >=

    public RelExp(ArrayList<AddExp> addExps, ArrayList<Integer> opTypes) {
        this.addExps = addExps;
        this.opTypes = opTypes;
    }

    public void print(StringBuilder strb) {
        int size = addExps.size();
        for (int i = 0; i < size; i++) {
            int opType = opTypes.get(i);
            if (opType == 1) {
                strb.append("LSS <\n");
            }
            else if (opType == 2) {
                strb.append("GRE >\n");
            }
            else if (opType == 3) {
                strb.append("LEQ <=\n");
            }
            else if (opType == 4) {
                strb.append("GEQ >=\n");
            }
            addExps.get(i).print(strb);
            strb.append("<RelExp>\n");
        }
    }
}

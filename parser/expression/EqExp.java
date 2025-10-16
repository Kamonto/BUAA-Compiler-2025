package parser.expression;

import java.util.ArrayList;

public class EqExp {
    private ArrayList<RelExp> relExps;
    private ArrayList<Integer> opTypes; // 0: first, 1: ==, 2: !=

    public EqExp(ArrayList<RelExp> relExps, ArrayList<Integer> opTypes) {
        this.relExps = relExps;
        this.opTypes = opTypes;
    }

    public void print(StringBuilder strb) {
        int size = relExps.size();
        for (int i = 0; i < size; i++) {
            int opType = opTypes.get(i);
            if (opType == 1) {
                strb.append("EQL ==\n");
            }
            else if (opType == 2) {
                strb.append("NEQ !=\n");
            }
            relExps.get(i).print(strb);
            strb.append("<EqExp>\n");
        }
    }
}

package parser.expression;

import java.util.ArrayList;

public class LAndExp {
    private ArrayList<EqExp> eqExps;

    public LAndExp(ArrayList<EqExp> eqExps) {
        this.eqExps = eqExps;
    }

    public void print(StringBuilder strb) {
        int size = eqExps.size();
        if (size > 0) {
            eqExps.get(0).print(strb);
            strb.append("<LAndExp>\n");
            for (int i = 1; i < size; i++) {
                strb.append("AND &&\n");
                eqExps.get(i).print(strb);
                strb.append("<LAndExp>\n");
            }
        }
    }
}

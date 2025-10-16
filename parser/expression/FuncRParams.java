package parser.expression;

import java.util.ArrayList;

public class FuncRParams {
    private ArrayList<Exp> exps;

    public FuncRParams(ArrayList<Exp> exps) {
        this.exps = exps;
    }

    public void print(StringBuilder strb) {
        int size = exps.size();
        if (size > 0) {
            exps.get(0).print(strb);
            for (int i = 1; i < size; i++) {
                strb.append("COMMA ,\n");
                exps.get(i).print(strb);
            }
        }
        strb.append("<FuncRParams>\n");
    }
}

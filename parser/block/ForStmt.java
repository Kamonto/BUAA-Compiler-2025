package parser.block;

import parser.expression.Exp;
import parser.expression.LVal;

import java.util.ArrayList;


public class ForStmt {
    private ArrayList<LVal> lVals;
    private ArrayList<Exp> exps;

    public ForStmt(ArrayList<LVal> lVals, ArrayList<Exp> exps) {
        this.lVals = lVals;
        this.exps = exps;
    }

    public void print(StringBuilder strb) {
        int size = lVals.size();
        if (size > 0) {
            lVals.get(0).print(strb);
            strb.append("ASSIGN =\n");
            exps.get(0).print(strb);
            for (int i = 1; i < size; i++) {
                strb.append("COMMA ,\n");
                lVals.get(i).print(strb);
                strb.append("ASSIGN =\n");
                exps.get(i).print(strb);
            }
        }
        strb.append("<ForStmt>\n");
    }
}

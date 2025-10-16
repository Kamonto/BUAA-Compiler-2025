package parser;

import parser.block.MainFuncDef;
import parser.declaration.Decl;
import parser.function.FuncDef;

import java.util.ArrayList;

public class CompUnit {
    private ArrayList<Decl> decls;
    private ArrayList<FuncDef> funcDefs;
    private MainFuncDef mainFuncDef;

    public CompUnit(ArrayList<Decl> decls, ArrayList<FuncDef> funcDefs, MainFuncDef mainFuncDef) {
        this.decls = decls;
        this.funcDefs = funcDefs;
        this.mainFuncDef = mainFuncDef;
    }

    public void print(StringBuilder strb) {
        for (Decl decl : decls) {
            decl.print(strb);
        }
        for (FuncDef funcDef : funcDefs) {
            funcDef.print(strb);
        }
        mainFuncDef.print(strb);
        strb.append("<CompUnit>\n");
    }
}

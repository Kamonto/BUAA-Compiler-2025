package parser.expression;

import llvmgenerator.LLVMTable;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class ConstExp {
    private AddExp addExp;

    public ConstExp (AddExp addExp) {
        this.addExp = addExp;
    }

    public void print(StringBuilder strb) {
        addExp.print(strb);
        strb.append("<ConstExp>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        addExp.symbolize(symbols, scope);
    }

    public int calculate(SymbolTable symbols, Scope scope) {
        return addExp.calculate(symbols, scope);
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {

    }
}

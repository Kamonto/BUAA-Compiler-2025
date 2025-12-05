package parser.block;

import lexer.Token;
import llvmgenerator.LLVM;
import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMDefStr;
import llvmgenerator.instruction.LLVMPutInt;
import llvmgenerator.instruction.LLVMPutStr;
import parser.expression.Exp;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class StmtPrint implements Stmt {
    private Token printfToken;
    private String stringConst;
    private ArrayList<Exp> exps;

    public StmtPrint(Token printfToken, String stringConst, ArrayList<Exp> exps) {
        this.printfToken = printfToken;
        this.stringConst = stringConst;
        this.exps = exps;
    }

    public void print(StringBuilder strb) {
        strb.append("PRINTFTK printf\n");
        strb.append("LPARENT (\n");
        strb.append("STRCON ").append(stringConst).append("\n");
        for (Exp exp : exps) {
            strb.append("COMMA ,\n");
            exp.print(strb);
        }
        strb.append("RPARENT )\n");
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        symbols.checkFormatCharInPrintfMismatch(stringConst, exps.size(), printfToken);
        for (Exp exp : exps) {
            exp.symbolize(symbols, scope);
        }
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        ArrayList<LLVM> llvmlist = new ArrayList<LLVM>();
        StringBuilder strb = new StringBuilder();
        int constsize = stringConst.length();
        int size = 0;
        int nowexp = 0;
        for (int i = 1; i < constsize - 1; i++) {
            char c = stringConst.charAt(i);
            if (c == '\\' && i < constsize - 2 && stringConst.charAt(i + 1) == 'n') {
                strb.append("\n");
                i++;
                size++;
            }
            else if (c == '%' && i < constsize - 2 && stringConst.charAt(i + 1) == 'd') {
                if (size > 0) {
                    strb.append("\0");
                    size++;
                    String label = "@str." + llvms.llvmDefStrsSize();
                    LLVMDefStr llvmDefStr = new LLVMDefStr(label, size, strb);
                    llvms.addLLVMDefStr(llvmDefStr);
                    llvmlist.add(new LLVMPutStr(size, label));
                    strb = new StringBuilder();
                    size = 0;
                }
                llvmlist.add(new LLVMPutInt(exps.get(nowexp).llvmGenerate(symbols, scope, llvms)));
                nowexp++;
                i++;
            }
            else {
                strb.append(c);
                size++;
            }
        }
        if (size > 0) {
            strb.append("\0");
            size++;
            String label = "@str." + llvms.llvmDefStrsSize();
            LLVMDefStr llvmDefStr = new LLVMDefStr(label, size, strb);
            llvms.addLLVMDefStr(llvmDefStr);
            llvmlist.add(new LLVMPutStr(size, label));
        }
        for (LLVM llvm : llvmlist) {
            llvms.addLLVM(llvm);
        }
    }
}

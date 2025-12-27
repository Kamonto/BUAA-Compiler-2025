import error.ErrorList;
import lexer.Lexer;
import lexer.Token;
import llvmgenerator.LLVMGenerator;
import llvmgenerator.LLVMTable;
import mipsgenerator.MIPSGenerator;
import mipsgenerator.MIPSTable;
import parser.CompUnit;
import parser.Parser;
import symbolizer.Symbolizer;
import symbolizer.SymbolTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("testfile.txt");
        String code = new String(Files.readAllBytes(path));

        ErrorList errorList = new ErrorList();

        Lexer lexer = new Lexer(code, errorList);
        ArrayList<Token> tokens = lexer.lex();
        if (errorList.hasError()) {
            errorList.printError();
        }
        else {
            lexer.printToken();
        }

        Parser parser = new Parser(tokens, errorList);
        CompUnit compUnit = parser.parse();
        if (errorList.hasError()) {
            errorList.printError();
        }
        else {
            parser.printAST();
        }

        Symbolizer symbolizer = new Symbolizer(compUnit, errorList);
        SymbolTable symbols = symbolizer.symbolize();
        if (errorList.hasError()) {
            errorList.printError();
            System.exit(-1);
        }
        else {
            symbolizer.printSymbols();
        }

        LLVMGenerator llvmGenerator = new LLVMGenerator(compUnit, symbols);
        LLVMTable llvms = llvmGenerator.llvmGenerate();
        llvmGenerator.printLLVMs();

        MIPSGenerator mipsGenerator = new MIPSGenerator(llvms);
        MIPSTable mipses = mipsGenerator.mipsGenerate();
        mipsGenerator.printMIPSes();
    }
}

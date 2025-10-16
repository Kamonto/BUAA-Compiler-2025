import error.ErrorList;
import lexer.Lexer;
import lexer.Token;
import parser.CompUnit;
import parser.Parser;

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
//        if (errorList.hasError()) {
//            errorList.printError();
//        }
//        else {
//            lexer.printToken();
//        }

        Parser parser = new Parser(tokens, errorList);
        CompUnit compUnit = parser.parse();
        if (errorList.hasError()) {
            errorList.printError();
        }
        else {
            parser.printAST();
        }
    }
}

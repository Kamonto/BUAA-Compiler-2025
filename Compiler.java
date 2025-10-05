import error.ErrorList;
import lexer.Lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Compiler {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("testfile.txt");
        String code = Files.readString(path);

        ErrorList errorList = new ErrorList();
        Lexer lexer = new Lexer(code, errorList);
        lexer.lex();
        lexer.printToken();
        errorList.printError();
    }
}

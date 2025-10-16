package lexer;

import error.Error;
import error.ErrorList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Lexer {
    private final String code;
    private final int size;
    private int nowat;
    private int nowline;
    private ArrayList<Token> tokens;
    private ErrorList errorList;

    public Lexer(String code, ErrorList errorList) {
        this.code = code;
        size = code.length();
        nowat = 0;
        nowline = 1;
        tokens = new ArrayList<Token>();
        this.errorList = errorList;
    }

    public ArrayList<Token> lex() {
        while (nowat < size) {
            Token token = getToken();
            if (token != null) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private Token getToken() {
        StringBuilder contentBuilder = new StringBuilder();
        char nowchar = getCharFromCode(nowat);
        if (isNonDigit(nowchar)) {
            contentBuilder.append(nowchar);
            nowat++;
            nowchar = getCharFromCode(nowat);
            while (isNonDigit(nowchar) || isDigit(nowchar)) {
                contentBuilder.append(nowchar);
                nowat++;
                nowchar = getCharFromCode(nowat);
            }
            String content = contentBuilder.toString();
            if (content.equals("const")) {
                return new Token(TokenType.CONSTTK, content, nowline);
            }
            else if (content.equals("int")) {
                return new Token(TokenType.INTTK, content, nowline);
            }
            else if (content.equals("static")) {
                return new Token(TokenType.STATICTK, content, nowline);
            }
            else if (content.equals("break")) {
                return new Token(TokenType.BREAKTK, content, nowline);
            }
            else if (content.equals("continue")) {
                return new Token(TokenType.CONTINUETK, content, nowline);
            }
            else if (content.equals("if")) {
                return new Token(TokenType.IFTK, content, nowline);
            }
            else if (content.equals("main")) {
                return new Token(TokenType.MAINTK, content, nowline);
            }
            else if (content.equals("else")) {
                return new Token(TokenType.ELSETK, content, nowline);
            }
            else if (content.equals("for")) {
                return new Token(TokenType.FORTK, content, nowline);
            }
            else if (content.equals("return")) {
                return new Token(TokenType.RETURNTK, content, nowline);
            }
            else if (content.equals("void")) {
                return new Token(TokenType.VOIDTK, content, nowline);
            }
            else if (content.equals("printf")) {
                return new Token(TokenType.PRINTFTK, content, nowline);
            }
            else {
                return new Token(TokenType.IDENFR, content, nowline);
            }
        }
        else if (isDigit(nowchar)) {
            contentBuilder.append(nowchar);
            nowat++;
            nowchar = getCharFromCode(nowat);
            while (isDigit(nowchar)) {
                contentBuilder.append(nowchar);
                nowat++;
                nowchar = getCharFromCode(nowat);
            }
            String content = contentBuilder.toString();
            return new Token(TokenType.INTCON, content, nowline);
        }
        else if (nowchar == '"') {
            contentBuilder.append(nowchar);
            nowat++;
            nowchar = getCharFromCode(nowat);
            while (nowat < size && nowchar != '"') {
                contentBuilder.append(nowchar);
                nowat++;
                nowchar = getCharFromCode(nowat);
            }
            contentBuilder.append(nowchar);
            nowat++;
            String content = contentBuilder.toString();
            return new Token(TokenType.STRCON, content, nowline);
        }
        else {
            contentBuilder.append(nowchar);
            nowat++;
            if (nowchar == '&') {
                if (getCharFromCode(nowat) == '&') {
                    contentBuilder.append('&');
                    nowat++;
                }
                else {
                    errorList.addError(new Error(nowline, 'a'));
                }
            }
            else if (nowchar == '|') {
                if (getCharFromCode(nowat) == '|') {
                    contentBuilder.append('|');
                    nowat++;
                }
                else {
                    errorList.addError(new Error(nowline, 'a'));
                }
            }
            else if (nowchar == '<' || nowchar == '>' || nowchar == '=' || nowchar == '!') {
                if (getCharFromCode(nowat) == '=') {
                    contentBuilder.append('=');
                    nowat++;
                }
            }
            else if (nowchar == '/') {
                if (getCharFromCode(nowat) == '/') {
                    nowat++;
                    while (nowat < size && getCharFromCode(nowat) != '\n') {
                        nowat++;
                    }
                    nowline++;
                    nowat++;
                    return null;
                }
                else if (getCharFromCode(nowat) == '*') {
                    nowat++;
                    while (nowat < size && (getCharFromCode(nowat) != '*' || getCharFromCode(nowat + 1) != '/')) {
                        nowat++;
                    }
                    nowat += 2;
                    return null;
                }
            }
            String content = contentBuilder.toString();
            if (content.equals("!")) {
                return new Token(TokenType.NOT, content, nowline);
            }
            else if (content.equals("&")) {
                return new Token(TokenType.AND, content, nowline);
            }
            else if (content.equals("&&")) {
                return new Token(TokenType.AND, content, nowline);
            }
            else if (content.equals("|")) {
                return new Token(TokenType.OR, content, nowline);
            }
            else if (content.equals("||")) {
                return new Token(TokenType.OR, content, nowline);
            }
            else if (content.equals("+")) {
                return new Token(TokenType.PLUS, content, nowline);
            }
            else if (content.equals("-")) {
                return new Token(TokenType.MINU, content, nowline);
            }
            else if (content.equals("*")) {
                return new Token(TokenType.MULT, content, nowline);
            }
            else if (content.equals("/")) {
                return new Token(TokenType.DIV, content, nowline);
            }
            else if (content.equals("%")) {
                return new Token(TokenType.MOD, content, nowline);
            }
            else if (content.equals("<")) {
                return new Token(TokenType.LSS, content, nowline);
            }
            else if (content.equals("<=")) {
                return new Token(TokenType.LEQ, content, nowline);
            }
            else if (content.equals(">")) {
                return new Token(TokenType.GRE, content, nowline);
            }
            else if (content.equals(">=")) {
                return new Token(TokenType.GEQ, content, nowline);
            }
            else if (content.equals("==")) {
                return new Token(TokenType.EQL, content, nowline);
            }
            else if (content.equals("!=")) {
                return new Token(TokenType.NEQ, content, nowline);
            }
            else if (content.equals(";")) {
                return new Token(TokenType.SEMICN, content, nowline);
            }
            else if (content.equals(",")) {
                return new Token(TokenType.COMMA, content, nowline);
            }
            else if (content.equals("(")) {
                return new Token(TokenType.LPARENT, content, nowline);
            }
            else if (content.equals(")")) {
                return new Token(TokenType.RPARENT, content, nowline);
            }
            else if (content.equals("[")) {
                return new Token(TokenType.LBRACK, content, nowline);
            }
            else if (content.equals("]")) {
                return new Token(TokenType.RBRACK, content, nowline);
            }
            else if (content.equals("{")) {
                return new Token(TokenType.LBRACE, content, nowline);
            }
            else if (content.equals("}")) {
                return new Token(TokenType.RBRACE, content, nowline);
            }
            else if (content.equals("=")) {
                return new Token(TokenType.ASSIGN, content, nowline);
            }
            else if (content.equals("\n")) {
                nowline++;
                return null;
            }
            else {
                return null;
            }
        }
    }

    private char getCharFromCode(int index) {
        if (index < size) {
            return code.charAt(index);
        }
        else {
            return '@';
        }
    }

    private boolean isNonDigit(char c) {
        return c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public void printToken() throws IOException {
        StringBuilder strb = new StringBuilder();
        for (Token token : tokens) {
            strb.append(token.getTokenType().toString()).append(" ").append(token.getContent()).append("\n");
        }
        Path path = Paths.get("lexer.txt");
        Files.write(path, strb.toString().getBytes());
    }
}

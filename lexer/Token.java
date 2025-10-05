package lexer;

public class Token {
    private TokenType tokenType;
    private String content;
    private int line;

    public Token(TokenType tokenType, String content, int line) {
        this.tokenType = tokenType;
        this.content = content;
        this.line = line;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getContent() {
        return content;
    }

    public int getLine() {
        return line;
    }
}

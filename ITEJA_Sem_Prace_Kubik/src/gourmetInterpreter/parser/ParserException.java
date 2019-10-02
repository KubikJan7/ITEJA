package gourmetInterpreter.parser;

import gourmetInterpreter.lexer.TokenTypeEnum;

public class ParserException extends Exception {

    private String message;
    private TokenTypeEnum tokenType = TokenTypeEnum.NONE;

    public ParserException(String message) {
        super(message);
        this.message = message;
    }

    public ParserException(String message, TokenTypeEnum tokenType) {
        super(message);
        this.message = message;
        this.tokenType = tokenType;
    }

    public TokenTypeEnum getTokenType() {
        return this.tokenType;
    }

    @Override
    public String toString() {
            return (tokenType == TokenTypeEnum.NONE) ? message : (message + tokenType.toString().toUpperCase());
    }
}

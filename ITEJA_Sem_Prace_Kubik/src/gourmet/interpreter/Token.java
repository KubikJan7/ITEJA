package gourmet.interpreter;

public class Token<V> {
    private TokenTypeEnum tokenType;
    private V tokenValue;

    public Token(TokenTypeEnum tokenType, V tokenValue) {
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
    }

    public TokenTypeEnum getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenTypeEnum tokenType) {
        this.tokenType = tokenType;
    }

    public V getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(V tokenValue) {
        this.tokenValue = tokenValue;
    }

    @Override
    public String toString() {
        return "" +tokenValue + " " + tokenType;
    }
    
}

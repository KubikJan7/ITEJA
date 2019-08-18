package gourmetInterpreter.parser;

import gourmetInterpreter.lexer.Token;
import gourmetInterpreter.lexer.TokenTypeEnum;
import java.util.Iterator;
import java.util.Stack;

public class Parser {

    private Stack<Token> tokenStack;
    private Iterator<Token> tokenStackItr;
    private Stack<ParseTree> expressionStack;
    private ParseTree parseTree;

    public Parser(Stack<Token> stack) {
        this.tokenStack = (Stack<Token>) stack.clone();
        tokenStackItr = tokenStack.iterator();
        parseTree = new ParseTree();
        expressionStack = new Stack<>();
    }

    private Token nextT;

    public Stack<ParseTree> parse() throws ParserException {
        while (tokenStackItr.hasNext()) {
            //System.out.println(itr.next());
            if (nextT != null) {
                if (nextT.getTokenType().equals(TokenTypeEnum.EOL)) {
                    break;
                }
            }
            expressionStack.push(nextExpression());
        }
        //lookahead = this.stack.get(0);
        return expressionStack;
    }

    private ParseTree nextExpression() throws ParserException {

        nextT = tokenStackItr.next();
        TokenTypeEnum type = nextT.getTokenType();
        if (type == TokenTypeEnum.EOL) { //end of expression
            return parseTree;
        }
        parseTree.clear();

        if (type.equals(TokenTypeEnum.TITLE) || type.equals(TokenTypeEnum.COMMENT)) {
            parseTree.insertRoot(nextT);
            return nextExpression();
        } else {
            throw new ParserException("Unexpected token: ", nextT.getTokenType());
        }
    }
}

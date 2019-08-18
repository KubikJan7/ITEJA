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
    }

    private Token nextT;
    public Stack<ParseTree> parse() throws ParserException {
        while (tokenStackItr.hasNext()) {
            //System.out.println(itr.next());
            nextT = tokenStackItr.next();
            expressionStack.push(nextExpression());
        }
        //lookahead = this.stack.get(0);
        return expressionStack;
    }

    private ParseTree nextExpression() throws ParserException {
        if (nextT.getTokenType() == TokenTypeEnum.EOL) {
            return parseTree;
        }
        nextT = tokenStackItr.next();
        throw new ParserException("Unexpected token: ");
    }
}

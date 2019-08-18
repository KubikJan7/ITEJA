package gourmetInterpreter.parser;

import gourmetInterpreter.lexer.Token;
import gourmetInterpreter.lexer.TokenTypeEnum;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {

    private Stack<Token> tokenStack;
    private Iterator<Token> tokenStackItr;
    private Stack<ParseTree> expressionStack;
    private ParseTree parseTree;

    public Parser(Stack<Token> stack) {
        this.tokenStack = stack;
        tokenStackItr = tokenStack.iterator();
        parseTree = new ParseTree();
        expressionStack = new Stack<>();
    }

    private Token nextT;
    private boolean endOfProgram;

    public Stack<ParseTree> parse() throws ParserException {
        while (tokenStackItr.hasNext()) {
            //System.out.println(itr.next());
            if (endOfProgram) {
                throw new ParserException("The program must be ended with SERVES expression!");
            }
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

        switch (type) {
            case TITLE:
            case COMMENT:
                parseTree.insertRoot(nextT);
                return nextExpression();
            case INGREDIENTS:
                tokenStackItr.next(); // skip the dot
                Token value;
                Token variable;
                for (int i = 0; i < countIngredients(); i++) {
                    parseTree.clear();
                    value = tokenStackItr.next();
                    tokenStackItr.next(); // skip ml or g
                    variable = tokenStackItr.next();
                    parseTree.insertRoot(new Token<>(TokenTypeEnum.ASSIGNMENT, ""));
                    parseTree.setRootAsCurrentToken();
                    parseTree.insertLeaf(variable);
                    parseTree.insertLeaf(value);
                    expressionStack.push(parseTree);
                }
                return nextExpression();
            case EQUIPMENT:
                tokenStackItr.next(); // skip the dot
                for (int i = 0; i < countEquipments(); i++) {
                    nextT = tokenStackItr.next();
                    parseTree.clear();
                    parseTree.insertRoot(nextT);
                    expressionStack.push(parseTree);
                }
                return nextExpression();
            case METHOD:
                tokenStackItr.next(); // skip the dot
                countMethodLines();
                return nextExpression();
            case SERVES:
                parseTree.insertRoot(nextT);
                parseTree.setRootAsCurrentToken();
                nextT = tokenStackItr.next();
                if (!nextT.getTokenType().equals(TokenTypeEnum.INTEGER)) {
                    defaultAction();
                }
                parseTree.insertLeaf(nextT);
                endOfProgram = true;
                return nextExpression();
            default:
                defaultAction();
        }
        return null;
    }

    private void defaultAction() throws ParserException {
        throw new ParserException("Unexpected token: ", nextT.getTokenType());
    }

    private int countIngredients() {
        int counter = 0;
        for (Token token : tokenStack) {
            if (token.getTokenType().equals(TokenTypeEnum.INTEGER) || token.getTokenType().equals(TokenTypeEnum.CHAR)) {
                counter++;
            } else if (token.getTokenType().equals((TokenTypeEnum.EQUIPMENT))) { //to avoid SERVES being included
                break;
            }
        }
        return counter;
    }

    private int countEquipments() {
        int counter = 0;
        boolean startCount = false;
        for (int i = 0; i < tokenStack.size(); i++) {
            if (tokenStack.get(i).getTokenType().equals(TokenTypeEnum.EQUIPMENT)) {
                startCount = true;
            } else if (tokenStack.get(i).getTokenType().equals(TokenTypeEnum.METHOD)) {
                break;
            }
            if (startCount) {
                if (tokenStack.get(i).getTokenType().equals(TokenTypeEnum.VARIABLE)) {
                    counter++;
                }
            }
        }
        return counter;
    }
    
    // will count dots in method block
    private int countMethodLines(){
        int counter = 0;
        return counter;
    }
}

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
        expressionStack = new Stack<>();
    }

    private Token nextT;
    private boolean endOfProgram;

    public Stack<ParseTree> parse() throws ParserException {
        ParseTree expression;
        while (tokenStackItr.hasNext()) {
            //System.out.println(itr.next());
            if (endOfProgram) {
                throw new ParserException("The program must be ended with SERVES expression!");
            }
//            if (nextT != null) {
//                if (nextT.getTokenType().equals(TokenTypeEnum.EOL)) {
//                    break;
//                }
//            }
            expression = nextExpression();
            if (expression != null) {
                expressionStack.push(expression);
            }
        }
        return expressionStack;
    }

    private ParseTree nextExpression() throws ParserException {
        nextT = tokenStackItr.next();
        TokenTypeEnum type = nextT.getTokenType();
        if (type == TokenTypeEnum.EOL) { //end of expression
            return parseTree;
        }
        parseTree = new ParseTree();

        switch (type) {
            case TITLE:
            case COMMENT:
                parseTree.insertRoot(nextT);
                return nextExpression();
            case INGREDIENTS:
                insertBlockNameExpression();
                tokenStackItr.next(); // skip the dot
                Token value;
                Token variable;
                for (int i = 0; i < countIngredients(); i++) {
                    parseTree = new ParseTree();
                    value = tokenStackItr.next();
                    if (eggLine - 1 != i) { //no egg keyword used
                        tokenStackItr.next(); // skip ml or g
                    }
                    variable = tokenStackItr.next();
                    parseTree.insertRoot(new Token<>(TokenTypeEnum.ASSIGNMENT, ""));
                    parseTree.setRootAsCurrentToken();
                    parseTree.insertLeaf(variable);
                    parseTree.insertLeaf(value);
                    expressionStack.push(parseTree);
                }
                return null;
            case EQUIPMENT:
                insertBlockNameExpression();
                tokenStackItr.next(); // skip the dot
                for (int i = 0; i < countEquipments(); i++) {
                    nextT = tokenStackItr.next();
                    parseTree = new ParseTree();
                    parseTree.insertRoot(nextT);
                    expressionStack.push(parseTree);
                }
                return null;
            case METHOD:
                insertBlockNameExpression();
                for (int i = 0; i < countMethodLines(); i++) {
                    tokenStackItr.next(); // skip the dot
                    nextT = tokenStackItr.next();
                    type = nextT.getTokenType();
                    switch (type) {
                        case PUT:
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            break;
                        case FOR:
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            break;
                        case REMOVE:
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            break;
                        case LIQUEFY:
                            break;
                        case SOLIDIFY:
                            break;
                        case POUR:
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            tokenStackItr.next();
                            break;
                        default:
                            break;
                    }
                }
                return null;
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

    private int eggLine; //integer to specify line where there is no ml or g keyword 

    private int countIngredients() {
        int counter = 0;
        for (Token token : tokenStack) {
            if (token.getTokenType().equals(TokenTypeEnum.INTEGER) || token.getTokenType().equals(TokenTypeEnum.CHAR)) {
                counter++;
            } else if (token.getTokenValue().equals("eggs") || token.getTokenValue().equals("egg")) {
                eggLine = counter;
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
    private int countMethodLines() {
        int counter = 0;
        boolean startCount = false;
        for (int i = 0; i < tokenStack.size(); i++) {
            if (tokenStack.get(i).getTokenType().equals(TokenTypeEnum.METHOD)) {
                startCount = true;
            } else if (tokenStack.get(i).getTokenType().equals(TokenTypeEnum.SERVES)) {
                break;
            }
            if (startCount) {
                if (tokenStack.get(i).getTokenType().equals(TokenTypeEnum.EOL)) {
                    counter++;
                }
            }
        }
        return --counter;// minus one dot after METHOD keyword
    }

    private void insertBlockNameExpression() {
        parseTree.insertRoot(nextT);
        expressionStack.push(parseTree);
    }
}

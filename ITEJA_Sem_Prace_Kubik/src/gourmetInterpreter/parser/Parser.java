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
        this.tokenStack = stack;
        tokenStackItr = tokenStack.iterator();
        expressionStack = new Stack<>();
    }

    private Token nextT;

    public Stack<ParseTree> parse() throws ParserException {
        ParseTree expression;
        while (tokenStackItr.hasNext()) {
            //System.out.println(itr.next());
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

    private boolean endOfLineExpected;

    private ParseTree nextExpression() throws ParserException {
        nextT = tokenStackItr.next();
        TokenTypeEnum type = nextT.getTokenType();
        if (type == TokenTypeEnum.EOL) { //end of expression
            endOfLineExpected = false;
            return parseTree;
        } else if (type != TokenTypeEnum.EOL && endOfLineExpected) {
            System.out.println((nextT));
            throw new ParserException("End of line expected, instead: ", type);
        }
        parseTree = new ParseTree();

        switch (type) {
            case TITLE:
            case COMMENT:
                parseTree.insertRoot(nextT);
                endOfLineExpected = true;
                return nextExpression();
            case INGREDIENTS:
                insertBlockNameExpression();
                skipDot();
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
                skipDot();
                for (int i = 0; i < countEquipments(); i++) {
                    nextT = tokenStackItr.next();
                    parseTree = new ParseTree();
                    parseTree.insertRoot(nextT);
                    expressionStack.push(parseTree);
                }
                return null;
            case METHOD:
                insertBlockNameExpression();
                int forExprCount = 0;
                for (int i = 0; i < countMethodLines() - forExprCount; i++) {
                    skipDot();
                    nextT = tokenStackItr.next();
                    type = nextT.getTokenType();
                    parseTree = new ParseTree();
                    parseTree.insertRoot(nextT);
                    parseTree.setRootAsCurrentToken();
                    expressionStack.push(parseTree);
                    switch (type) {
                        case PUT:
                            putIngredientIntoBowl();
                            break;
                        case FOR:
                            Token var = tokenStackItr.next(); // variable
                            tokenStackItr.next(); //each
                            Token in = tokenStackItr.next(); //in
                            tokenStackItr.next();//the
                            Token bowl = tokenStackItr.next(); // bowl
                            parseTree.insertLeaf(in);
                            parseTree.setChildAsCurrentToken(0);
                            parseTree.insertLeaf(var);
                            parseTree.insertLeaf(bowl);
                            parseTree.setRootAsCurrentToken();
                            parseTree.insertLeaf(new Token(TokenTypeEnum.BODY, ""));
                            parseTree.setChildAsCurrentToken(1);
                            tokenStackItr.next(); //dot
                            nextT = tokenStackItr.next();
                            type = nextT.getTokenType();
                            while (!type.equals(TokenTypeEnum.FOR)) {
                                parseTree.insertLeaf(nextT);
                                parseTree.setChildAsCurrentToken(forExprCount);
                                forExprCount++;
                                switch (type) {
                                    case PUT:
                                        putIngredientIntoBowl();
                                        break;
                                    case REMOVE:
                                        removeIngredientFromBowl();
                                        break;
                                    default:
                                        unexpectedTokenException();
                                }
                                parseTree.setParentAsCurrentToken(); //set BODY as current node
                                tokenStackItr.next(); //dot                                
                                nextT = tokenStackItr.next();
                                type = nextT.getTokenType();
                            }
                            tokenStackItr.next(); //each
                            tokenStackItr.next(); //ingredient
                            tokenStackItr.next(); //end
                            forExprCount++;
                            break;
                        case REMOVE:
                            removeIngredientFromBowl();
                            break;
                        case LIQUEFY:
                            tokenStackItr.next(); //contents
                            tokenStackItr.next(); //of
                            tokenStackItr.next(); //the
                            nextT = tokenStackItr.next(); //bowl
                            parseTree.insertLeaf(nextT);
                            break;
                        case SOLIDIFY:
                            tokenStackItr.next(); //contents
                            tokenStackItr.next(); //of
                            tokenStackItr.next(); //the
                            nextT = tokenStackItr.next(); //bowl
                            parseTree.insertLeaf(nextT);
                            break;
                        case POUR:
                            tokenStackItr.next(); //contents
                            tokenStackItr.next(); //of
                            tokenStackItr.next(); //the
                            nextT = tokenStackItr.next(); //bowl
                            parseTree.insertLeaf(nextT);
                            tokenStackItr.next(); //into
                            tokenStackItr.next(); //the
                            tokenStackItr.next(); //baking
                            nextT = tokenStackItr.next(); //dish
                            parseTree.insertLeaf(nextT);
                            break;
                        default:
                            unexpectedTokenException();
                    }
                }
                skipDot();
                return null;
            case SERVES:
                parseTree.insertRoot(nextT);
                parseTree.setRootAsCurrentToken();
                nextT = tokenStackItr.next();
                if (!nextT.getTokenType().equals(TokenTypeEnum.INTEGER)) {
                    unexpectedTokenException();
                }
                parseTree.insertLeaf(nextT);
                return nextExpression();
            default:
                unexpectedTokenException();
        }
        return null;
    }

    private void unexpectedTokenException() throws ParserException {
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

    private void removeIngredientFromBowl() {
        nextT = tokenStackItr.next(); //ingredient
        parseTree.insertLeaf(nextT);
        tokenStackItr.next(); //from
        tokenStackItr.next(); //the
        nextT = tokenStackItr.next(); //bowl
        parseTree.insertLeaf(nextT);
    }

    private void putIngredientIntoBowl() {
        nextT = tokenStackItr.next(); //ingredient
        parseTree.insertLeaf(nextT);
        tokenStackItr.next(); //into
        tokenStackItr.next(); //the
        nextT = tokenStackItr.next(); //bowl
        parseTree.insertLeaf(nextT);
    }

    private void skipDot() throws ParserException {
        Token t = tokenStackItr.next(); // skip the dot
        if (t.getTokenType() != TokenTypeEnum.EOL) {
            throw new ParserException("End of line expected instead of: ", t.getTokenType());
        }
    }
}

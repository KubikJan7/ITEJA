package gourmetInterpreter.parser;

import gourmetInterpreter.lexer.Token;
import gourmetInterpreter.lexer.TokenTypeEnum;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class Evaluator {

    private Stack<ParseTree> expressionStack;
    public HashMap<String, Object> ingredients;
    public HashMap<String, Stack<String>> bowls;

    private String recipeTitle;
    private String recipeDesc;

    public Evaluator(Stack<ParseTree> expressionStack) {
        this.expressionStack = expressionStack;
        this.ingredients = new HashMap<>();
        this.bowls = new HashMap<>();
    }

    public void evaluate() throws ParserException {
        for (ParseTree parseTree : expressionStack) {
            Iterator<Token> itr = parseTree.iterator();
            while (itr.hasNext()) {
                Token t = itr.next();
                TokenTypeEnum type = t.getTokenType();
                switch (type) {
                    case TITLE:
                        recipeTitle = (String) t.getTokenValue();
                        break;
                    case COMMENT:
                        recipeDesc = (String) t.getTokenValue();
                        break;
                    case ASSIGNMENT:
                        String key = (String) itr.next().getTokenValue();
                        Token token = itr.next();
                        int value = Integer.parseInt(token.getTokenValue().toString());
                        TokenTypeEnum valueType = token.getTokenType();
                        ingredients.put(key, (valueType.equals(TokenTypeEnum.INTEGER)) ? value : String.valueOf((char) value)); //ml will be saved as integer, g as string converted from char
                        break;
                    case DECLARATION:
                        bowls.put(itr.next().getTokenValue().toString(), new Stack());
                        break;
                    case PUT:
                        if(parseTree.getRootData().getTokenType().equals(TokenTypeEnum.FOR)){
                            break;
                        }
                        Token ingredient = itr.next();
                        Token bowl = itr.next();
                        Stack s = bowls.get(bowl.getTokenValue().toString());
                        if (s!=null) {
                            s.push(ingredient.getTokenValue());
                        } else {
                            throw new ParserException("Ingredient " + ingredient.getTokenValue() + "cannot be added to bowl which is not declared!");
                        }
                        break;
                }
            }
        }
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getRecipeDesc() {
        return recipeDesc;
    }

}

package gourmetInterpreter.parser;

import gourmetInterpreter.lexer.Token;
import gourmetInterpreter.lexer.TokenTypeEnum;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class Evaluator {

    private Stack<ParseTree> expressionStack;
    public HashMap<String, Object> ingredients;
    private HashMap<String, Stack> bowls;

    private String recipeTitle;
    private String recipeDesc;

    public Evaluator(Stack<ParseTree> expressionStack) {
        this.expressionStack = expressionStack;
        this.ingredients = new HashMap<>();
        this.bowls = new HashMap<>();
    }

    public void evaluate() {
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
                        ingredients.put(key, (valueType.equals(TokenTypeEnum.INTEGER)) ? value : String.valueOf((char)value)); //ml will be saved as integer, g as string converted from char
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

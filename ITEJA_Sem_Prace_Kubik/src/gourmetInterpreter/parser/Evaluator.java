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
                        if (parseTree.getRootData().getTokenType().equals(TokenTypeEnum.FOR)) {
                            break;
                        }
                        Token ingredient = itr.next();
                        Token bowl = itr.next();
                        Stack<String> stack = bowls.get(bowl.getTokenValue().toString());
                        if (stack != null) {
                            stack.push(ingredient.getTokenValue().toString());
                        } else {
                            throw new ParserException("Ingredient " + ingredient.getTokenValue() + " cannot be added to bowl which is not declared!");
                        }
                        break;
                    case REMOVE:
                        if (parseTree.getRootData().getTokenType().equals(TokenTypeEnum.FOR)) {
                            break;
                        }
                        ingredient = itr.next();
                        bowl = itr.next();
                        stack = bowls.get(bowl.getTokenValue().toString());
                        if (stack != null) {
                            System.out.println(stack.remove(ingredient.getTokenValue().toString()));
                        } else {
                            throw new ParserException("Ingredient " + ingredient.getTokenValue() + " cannot be removed from bowl which is not declared!");
                        }
                        break;
                    case FOR:
                        break;
                    case LIQUEFY:
                        bowl = itr.next();
                        stack = bowls.get(bowl.getTokenValue().toString());
                        if (stack != null) {
                            for (String str : stack) {
                                Object obj = ingredients.get(str);
                                if (obj instanceof String) {
                                    ingredients.put(str, (int) obj.toString().charAt(0));
                                } else {
                                    ingredients.put(str, obj);
                                }
                            }
                        } else {
                            throw new ParserException("Ingredients of the " + bowl.getTokenValue() + " cannot be liquefy because this bowl is not declared!");
                        }
                        break;
                    case SOLIDIFY:
                        bowl = itr.next();
                        stack = bowls.get(bowl.getTokenValue().toString());
                        if (stack != null) {
                            for (String str : stack) {
                                Object obj = ingredients.get(str);
                                System.out.println(obj instanceof Integer);
                                if (obj instanceof Integer) {
                                    ingredients.put(str, String.valueOf((char)Integer.parseInt(obj.toString())));
                                } else {
                                    ingredients.put(str, obj);
                                }
                            }
                        } else {
                            throw new ParserException("Ingredients of the " + bowl.getTokenValue() + " cannot be solidify because this bowl is not declared!");
                        }
                        break;
                    case COMBINE:
                        bowl = itr.next();
                        stack = bowls.get(bowl.getTokenValue().toString());
                        int sum = 0;
                        if (stack != null) {
                            for (String str : stack) {
                                try {
                                    sum += (int) ingredients.get(str);
                                } catch (ClassCastException e) {
                                    throw new ParserException("All ingredients must be transfered to liquid state first in order to execute COMBINE operation!");
                                }
                            }
                            stack.clear();
                            stack.push("sum");
                            ingredients.put("sum", sum);
                        } else {
                            throw new ParserException("Ingredients of the " + bowl.getTokenValue() + " cannot be combined because this bowl is not declared!");
                        }
                        break;
                    case POUR:
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

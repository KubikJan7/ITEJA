package gourmetInterpreter.parser;

import gourmetInterpreter.lexer.Token;
import gourmetInterpreter.lexer.TokenTypeEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class Evaluator {

    private Stack<ParseTree> expressionStack;
    public HashMap<String, Object> ingredients;
    public HashMap<String, Stack<String>> bowls;
    public Stack baking_dish;

    private String recipeTitle;
    private String recipeDesc;

    public Evaluator(Stack<ParseTree> expressionStack) {
        this.expressionStack = expressionStack;
        this.ingredients = new HashMap<>();
        this.bowls = new HashMap<>();
        this.baking_dish = new Stack();
    }

    public void evaluate() throws ParserException {
        for (ParseTree parseTree : expressionStack) {
            Iterator<Token> itr = parseTree.iterator();
            while (itr.hasNext()) {
                Token treeToken = itr.next();
                TokenTypeEnum type = treeToken.getTokenType();
                switch (type) {
                    case TITLE:
                        recipeTitle = (String) treeToken.getTokenValue();
                        break;
                    case COMMENT:
                        recipeDesc = (String) treeToken.getTokenValue();
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
                        Stack<String> bowlStack = bowls.get(bowl.getTokenValue().toString());
                        if (bowlStack != null) {
                            bowlStack.push(ingredient.getTokenValue().toString());
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
                        bowlStack = bowls.get(bowl.getTokenValue().toString());
                        if (bowlStack != null) {
                            bowlStack.remove(ingredient.getTokenValue().toString());
                        } else {
                            throw new ParserException("Ingredient " + ingredient.getTokenValue() + " cannot be removed from bowl which is not declared!");
                        }
                        break;
                    case FOR:
                        ArrayList<TokenTypeEnum> operations = new ArrayList<>();
                        ArrayList<String> putParameters = new ArrayList<>();
                        ArrayList<String> removeParameters = new ArrayList<>();
                        itr.next();//in
                        itr.next();//block
                        ingredient = itr.next();
                        bowl = itr.next();
                        bowlStack = bowls.get(bowl.getTokenValue().toString());
                        treeToken = itr.next();
                        type = treeToken.getTokenType();
                        while (!type.equals(TokenTypeEnum.VARIABLE)) {
                            switch (type) {
                                case PUT:
                                    operations.add(TokenTypeEnum.PUT);
                                    break;
                                case REMOVE:
                                    operations.add(TokenTypeEnum.REMOVE);
                                    break;
                                default:
                                    throw new ParserException("Unxpected keyword found inside FOR cycle:" + type);
                            }
                            treeToken = itr.next();
                            type = treeToken.getTokenType();
                        }
                        for (TokenTypeEnum operation : operations) {
                            switch (operation) {
                                case PUT:
                                    putParameters.add((String) treeToken.getTokenValue()); //ingredient
                                    putParameters.add((String) itr.next().getTokenValue()); //bowl
                                    break;
                                case REMOVE:
                                    removeParameters.add((String) treeToken.getTokenValue()); //ingredient
                                    removeParameters.add((String) itr.next().getTokenValue()); //bowl
                                    break;
                            }
                            if (itr.hasNext()) {
                                treeToken = itr.next();
                            }
                        }
                        if (bowlStack != null) {
                            for (int i = 0; i < bowlStack.size(); i++) {
                                if (operations.isEmpty()) {
                                    break;
                                }
                                for (TokenTypeEnum operation : operations) {
                                    Stack<String> stack;
                                    switch (operation) {
                                        case PUT:
                                            stack = bowls.get(putParameters.get(1));
                                            if (stack != null) {
                                                stack.push(bowlStack.get(i));
                                            } else {
                                                throw new ParserException("Ingredient " + ingredient.getTokenValue() + " cannot be added to bowl which is not declared!");
                                            }
                                            break;
                                        case REMOVE:
                                            stack = bowls.get(removeParameters.get(1));
                                            if (stack != null) {
                                                stack.remove(bowlStack.get(i));
                                                --i; //because the remove operation is decreasing the stack index
                                            } else {
                                                throw new ParserException("Ingredient " + ingredient.getTokenValue() + " cannot be removed from bowl which is not declared!");
                                            }
                                            break;
                                    }
                                }
                            }
                        } else {
                            throw new ParserException("For cycle cannot be applied on a bowl which is not declared!");
                        }
                        break;
                    case LIQUEFY:
                        bowl = itr.next();
                        bowlStack = bowls.get(bowl.getTokenValue().toString());
                        if (bowlStack != null) {
                            for (String str : bowlStack) {
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
                        bowlStack = bowls.get(bowl.getTokenValue().toString());
                        if (bowlStack != null) {
                            for (String str : bowlStack) {
                                Object obj = ingredients.get(str);
                                if (obj instanceof Integer) {
                                    ingredients.put(str, String.valueOf((char) Integer.parseInt(obj.toString())));
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
                        bowlStack = bowls.get(bowl.getTokenValue().toString());
                        int sum = 0;
                        if (bowlStack != null) {
                            for (String str : bowlStack) {
                                try {
                                    sum += (int) ingredients.get(str);
                                } catch (ClassCastException e) {
                                    throw new ParserException("All ingredients must be transfered to liquid state first in order to execute COMBINE operation!");
                                }
                            }
                            bowlStack.clear();
                            bowlStack.push("sum");
                            ingredients.put("sum", sum);
                        } else {
                            throw new ParserException("Ingredients of the " + bowl.getTokenValue() + " cannot be combined because this bowl is not declared!");
                        }
                        break;
                    case POUR:
                        bowl = itr.next();
                        bowlStack = bowls.get(bowl.getTokenValue().toString());
                        if (bowlStack != null) {
                            for (String str : bowlStack) {
                                baking_dish.push(ingredients.get(str));
                            }
                        } else {
                            throw new ParserException("Ingredients of the " + bowl.getTokenValue() + " cannot be combined because this bowl is not declared!");
                        }
                        break;
                    case SERVES:
                        printCount = (int) itr.next().getTokenValue();
                        break;
                }
                break;
            }
        }
    }

    private int printCount;

    public void printOutput() {
        System.out.println("-------");
        System.out.print("Output: ");
        System.out.print("\n-------");
        for (int i = 0; i < printCount; i++) {
            System.out.println("");
            for (Object object : baking_dish) {
                if (object instanceof String) {
                    System.out.print(object);
                } else {
                    System.out.print(object + " ");
                }
            }
        }
        System.out.println("\n-------");
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getRecipeDesc() {
        return recipeDesc;
    }

}

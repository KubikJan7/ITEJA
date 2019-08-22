package gourmetInterpreter.parser;

import gourmetInterpreter.lexer.Token;
import gourmetInterpreter.lexer.TokenTypeEnum;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class Evaluator {

    private HashMap<String, Object> ingredients;
    private HashMap<String, Stack> bowls;

    private String recipeTitle;
    private String recipeDesc;
    
    public Evaluator() {
        this.ingredients = new HashMap<>();
        this.bowls = new HashMap<>();
    }

    public void evaluate(Stack<ParseTree> expressionStack) {
        for (ParseTree parseTree : expressionStack) {
            Iterator<Token> itr = parseTree.iterator();
            while (itr.hasNext()) {
                Token t = itr.next();
                TokenTypeEnum type = t.getTokenType();
            }
        }
    }
}

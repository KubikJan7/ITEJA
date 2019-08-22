package gourmetInterpreter.parser;

import gourmetInterpreter.lexer.Token;
import java.util.Iterator;
import java.util.Stack;

public class Evaluator {

    public void evaluate(Stack<ParseTree> expressionStack) {
        for (ParseTree parseTree : expressionStack) {
            Iterator<Token> itr = parseTree.iterator();
            while(itr.hasNext()){
                itr.next();
            }
        }
    }
}

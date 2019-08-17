package gourmetInterpreter.parser;

import gourmetInterpreter.lexer.Token;
import java.util.Iterator;
import java.util.Stack;

public class Parser {

    private Stack<Token> stack;
    private Token lookahead;

    public Parser(Stack<Token> stack) {
        this.stack = (Stack<Token>) stack.clone();

    }

    public void parse() {
//        Iterator itr = stack.iterator();
//        while(itr.hasNext()){
//            //System.out.println(itr.next());
//            itr.next();
//        }
        lookahead = this.stack.get(0);
    }
}

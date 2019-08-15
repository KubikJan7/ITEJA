package interpreter;

import java.util.Stack;

public class Lexer {

    private Stack<Token> stack;

    public Lexer() {
        stack = new Stack();
    }

    public void CreateListOfTokens(String code) {
        code = code.toLowerCase();
        String[] words = code.split(" ");

        for (String word : words) {
            
        }
    }
}

package gourmetInterpreter;

import java.util.Stack;

public class Lexer {

    private Stack<Token> stack;
    private int index; //index used for going through the words in the code

    public Lexer() {
        stack = new Stack();
    }

    public void createListOfTokens(String code) {
        code = code.toLowerCase();
        String[] words = code.split("(?=[.])|\\s+"); // will seperate dots to new words and erase all white spaces
        String word;

        stack.push(new Token<>(TokenTypeEnum.TITLE, getStringBeforeNextDot(words)));
        stack.push(new Token<>(TokenTypeEnum.EOL, words[index++])); //store the dot

        stack.push(new Token<>(TokenTypeEnum.COMMENT, getStringBeforeNextDot(words)));
        stack.push(new Token<>(TokenTypeEnum.EOL, words[index++])); //store the dot

        for (int i = index; i < words.length; i++) {
            word = words[i];
            if (TokenTypeEnum.isKeyword(word)) {
                stack.push(new Token(TokenTypeEnum.getKeyword(word), word));
            } else if (isNumeric(word)) {
                if (word.contains(".")) {
                    throw new NumberFormatException("Double data types are not allowed in Gourmet");
                } else {
                    if (words[i + 1].equals(TokenTypeEnum.ML.toString())||words[i-1].equals(TokenTypeEnum.SERVES.toString())) {
                        stack.push(new Token(TokenTypeEnum.INTEGER, word));
                    } else {
                        stack.push(new Token(TokenTypeEnum.CHAR, word));
                    }
                }
            } else {
                stack.push(new Token(TokenTypeEnum.VARIABLE, word));
            }

        }
    }

    private String getStringBeforeNextDot(String[] strArray) {
        String str = "";
        while (!strArray[index].equals(".")) {
            str += strArray[index++] + " ";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public Stack<Token> getStack() {
        return stack;
    }

    private boolean isNumeric(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

package gourmetInterpreter;

import gourmetInterpreter.lexer.FileReader;
import gourmetInterpreter.lexer.Lexer;
import gourmetInterpreter.parser.Parser;
import java.io.FileNotFoundException;
import java.io.IOException;
import gourmetInterpreter.parser.Evaluator;
import gourmetInterpreter.parser.ParserException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException, ParserException {
        String path = "./gourmet_sample_0.txt";
        FileReader fileReader = new FileReader(path);
        String code = fileReader.loadRecipe();
        Lexer lexer = new Lexer();
        Parser parser = new Parser(lexer.findTokens(code));
        Evaluator evaluator = new Evaluator(parser.parse());
        evaluator.evaluate();
        //System.out.println("------------ Gourmet interpreter ------------");
        System.out.print("------------\n");
        System.out.println("Recipe name ");
        System.out.print("------------\n");
        System.out.println(evaluator.getRecipeTitle());
        System.out.print("-------------------");
        System.out.println("\nRecipe description");
        System.out.print("-------------------\n");
        printString(evaluator.getRecipeDesc().split(" "), 6);
        evaluator.printOutput();
    }

    private static void printString(String[] s, int wSCount) {
        for (int i = 0; i < s.length; i++) {
            if (s[i].equals(s[s.length - 1])) {
                System.out.print(s[i]);
            } else {
                System.out.print(s[i] + " ");
            }
            if ((i + 1) % wSCount == 0) {
                System.out.println();
            }
        }
        System.out.println(".");
    }
}

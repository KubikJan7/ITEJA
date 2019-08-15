package gourmet.interpreter;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileReader fileReader = new FileReader("./gourmet_sample_0.txt");
        String code = fileReader.loadRecipe();
        //System.out.print(code);
        Lexer lex = new Lexer();
        lex.createListOfTokens(code);
        for (Token tok : lex.getStack()) {
            System.out.println(tok);
        }
    }

}

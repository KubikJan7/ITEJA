package gourmetInterpreter;

import gourmetInterpreter.lexer.FileReader;
import gourmetInterpreter.lexer.Lexer;
import gourmetInterpreter.lexer.Token;
import gourmetInterpreter.parser.Parser;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileReader fileReader = new FileReader("./gourmet_sample_0.txt");
        String code = fileReader.loadRecipe();
        //System.out.print(code);
        Lexer lexer = new Lexer();
        Parser parser = new Parser(lexer.findTokens(code));
        parser.parse();
        
    }

}

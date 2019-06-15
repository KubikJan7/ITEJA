package interpreter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;

public class FileReader {

    private int line;
    private BufferedReader reader;
    char characterIn;

    public FileReader(String filename) throws FileNotFoundException {
        reader = new BufferedReader(new java.io.FileReader(filename));
    }

    public Stack<Character> loadRecipe() throws FileNotFoundException, IOException {

        Stack<Character> source = new Stack<>();
        while ((line = reader.read()) != -1) {
            characterIn = (char) line;
            source.push(characterIn);
        }
        return source;
    }
}

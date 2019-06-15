package interpreter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileReader fileReader = new FileReader("./gourmet_sample_0.txt");
        Stack<Character> source = fileReader.loadRecipe();
        for (Object c : source) {
            System.out.print((char) c);
        }
    }

}

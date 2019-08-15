package interpreter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader {

    private String line;
    private BufferedReader reader;
    String code = "";

    public FileReader(String filename) throws FileNotFoundException {
        reader = new BufferedReader(new java.io.FileReader(filename));
    }

    public String loadRecipe() throws FileNotFoundException, IOException {
        while ((line = reader.readLine()) != null) {
            code += line + "\n"; 
        }
        return code;
    }
}

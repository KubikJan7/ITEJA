package gourmetInterpreter;

import gourmetInterpreter.lexer.FileReader;
import gourmetInterpreter.lexer.Lexer;
import gourmetInterpreter.lexer.Token;
import gourmetInterpreter.parser.Parser;
import java.io.FileNotFoundException;
import java.io.IOException;
import gourmetInterpreter.ads.AbstrTree;
import gourmetInterpreter.lexer.TokenTypeEnum;
import gourmetInterpreter.parser.ParseTree;
import gourmetInterpreter.parser.ParserException;
import java.util.Iterator;
import java.util.Stack;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException, ParserException {
        FileReader fileReader = new FileReader("./gourmet_sample_0.txt");
        String code = fileReader.loadRecipe();
        //System.out.print(code);
        Lexer lexer = new Lexer();
        Parser parser = new Parser(lexer.findTokens(code));
        Stack<ParseTree> stack = parser.parse();
        for (ParseTree p : stack) {
            Iterator itr = p.iterator();
            while(itr.hasNext()){
                System.out.println(itr.next());
            }
        }
        
        
//        AbstrTree<String> tree = new AbstrTree<>();
//        tree.insertRoot("c");
//        tree.setRootAsCurrentRoot();
//        tree.insertLeaf("x");
//        tree.insertLeaf("f");
//        tree.insertLeaf("t");
//        tree.setChildAsCurrentNode(0);
//        tree.insertLeaf("a");
//        tree.insertLeaf("s");
//        tree.setParentAsCurrentNode();
//        tree.setChildAsCurrentNode(2);
//        tree.insertLeaf("w");
//        tree.insertLeaf("o");
//        tree.insertLeaf("p");
//        tree.setChildAsCurrentNode(0);
//        tree.insertLeaf("b");
//        
//        Iterator<String> itr = tree.iterator();
//        while(itr.hasNext()){
//            System.out.print(itr.next() + ", ");
//        } 
    }

}

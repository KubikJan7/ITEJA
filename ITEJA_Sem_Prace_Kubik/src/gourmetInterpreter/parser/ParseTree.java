package gourmetInterpreter.parser;

import gourmetInterpreter.ads.AbstrTree;
import gourmetInterpreter.lexer.Token;
import java.util.Iterator;

public class ParseTree {

    AbstrTree<Token> tree = new AbstrTree<>();

    public void clear() {
        tree.clear();
    }

    public boolean isEmpty() {
        return tree.isEmpty();
    }

    public int getTokenCount() {
        return tree.getNodeCount();
    }

    public void insertRoot(Token root) {
        tree.insertRoot(root);
    }

    public void insertLeaf(Token leaf) {
        tree.insertLeaf(leaf);
    }

    public Token setRootAsCurrentToken() {
        return tree.setRootAsCurrentRoot();
    }

    public Token setChildAsCurrentToken(int index) {
        return tree.setChildAsCurrentNode(index);
    }

    public Token setParentAsCurrentToken() {
        return tree.setParentAsCurrentNode();
    }

    public Iterator<Token> iterator() {
        return tree.iterator();
    }

    public Token getRootData() {
        return tree.getRootNodeData();
    }
}

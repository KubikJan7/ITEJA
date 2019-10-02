package gourmetInterpreter.ads;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class AbstrTree<E> {

    private int nodeCount;
    private Node root;
    private Node current;

    public void clear() {
        root = null;
        current = null;
        nodeCount = 0;
    }

    public boolean isEmpty() {
        return nodeCount == 0;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void insertRoot(E data) {
        if (root != null) {
            throw new UnsupportedOperationException("Root node already exists.");
        }
        root = new Node(null, null, data);
        nodeCount++;
    }

    public void insertLeaf(E data) {
        if (current == null) {
            throw new NullPointerException("Root node is not initialized.");
        }
        Node child = new Node(current, null, data);
        if (current.children == null) {
            current.children = new ArrayList<>();
        }
        current.children.add(child);
        nodeCount++;
    }

    public E removeRoot() {
        if (nodeCount > 1) {
            throw new UnsupportedOperationException("The root cannot be removed "
                    + "if it contains children.");
        }
        Node u = root;
        clear();
        return u.data;
    }

    public E setRootAsCurrentRoot() {
        if (isEmpty()) {
            throw new NullPointerException("The root is not initialized.");
        }
        current = root;
        return root.data;
    }

    public E setChildAsCurrentNode(int index) {
        if (current == null) {
            throw new NullPointerException("Current node is not initialized.");
        } else if (current.children.isEmpty()) {
            throw new NoSuchElementException("Current node does not have any children.");
        } else if (index < 0 || index >= current.children.size()) {
            throw new NoSuchElementException("Given index is not valid.");
        }

        Node u;
        Iterator<Node> itr = current.children.iterator();
        int i = 0;
        while (itr.hasNext()) {
            u = itr.next();
            if (i == index) {
                current = u;
                return u.data;
            }
            i++;
        }
        throw new NullPointerException("The element with the given index does not exist.");
    }

    public E setParentAsCurrentNode() {
        if (current == null) {
            throw new NullPointerException("Current node is not initialized.");
        }
        if (current.parent == null) {
            throw new NullPointerException("Current node does not have a parent");
        }
        current = current.parent;
        return current.data;
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Stack<Node> stack = new Stack<>();
            private Node node = root;
            private E data;

            @Override
            public boolean hasNext() {
                if (node == root) {
                    return true;
                }
                return !stack.isEmpty();
            }

            @Override
            public E next() {
                if (node == root) {
                    stack.push(node);
                }
                node = stack.pop();
                data = node.data;
                if (node.children != null) {
                    Iterator<Node> itr = node.children.iterator();
                    while (itr.hasNext()) {
                        try {
                            stack.add(0, itr.next()); //works like queue -> breadth-first search
                        } catch (NoSuchElementException ex) {
                        }
                        //itr.next();
                    }
                }
                node = null;
                return data;
            }
        };
    }

    public E getRootNodeData() {
        return root.data;
    }

    public class Node {

        E data;
        Node parent;
        ArrayList<Node> children;

        public Node(Node parent, ArrayList<Node> children, E data) {
            this.data = data;
            this.parent = parent;
            this.children = children;
        }

    }
}

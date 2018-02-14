/******************************************************************************
 * Author: Dennis Tran
 * Custom Trie implementation for speed
 * Written: 11/18/17
 * For Coursera/Princeton Algorithms 2
 ******************************************************************************/
public class OptimizedTrie {
    private static final int R = 26;

    private Node root;
    private int n;

    public static class Node {
        public Node[] next = new Node[R];
        public String string;
    }

    public OptimizedTrie() {}

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.string != null;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 'A'], key, d+1);
    }

    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.string != null) n++;
            x.string = key;
        }
        else {
            char c = key.charAt(d);
            x.next[c - 'A'] = add(x.next[c - 'A'], key, d+1);
        }
        return x;
    }

    public Node root() {
        return root;
    }
}

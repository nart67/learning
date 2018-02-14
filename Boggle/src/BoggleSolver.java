/******************************************************************************
 * Author: Dennis Tran
 * Boggle board solver implementation
 * Written: 11/18/17
 * For Coursera/Princeton Algorithms 2
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver
{
    private final OptimizedTrie trie;
    private final OptimizedTrie.Node root;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trie = new OptimizedTrie();
        for (String s : dictionary) {
            trie.add(s);
        }
        root = trie.root();
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> set = new SET<>();
        OptimizedTrie.Node node = root;
        boolean[][] marked = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                char c = board.getLetter(i, j);
                OptimizedTrie.Node next = node.next[c - 'A'];
                if (c == 'Q') next = next.next['U' - 'A'];
                dfs(board, i, j, next, marked, set);
            }
        }
        return set;
    }

    private void dfs(BoggleBoard board, int row, int col, OptimizedTrie.Node node,
                     boolean[][] marked, SET<String> set) {
        if (node == null) return;
        marked[row][col] = true;
        if (node.string != null && node.string.length() > 2) set.add(node.string);
        for (int i = row - 1; i <= row + 1; i++) {
            if (i < 0 || i >= marked.length) continue;
            for (int j = col - 1; j <= col + 1; j++) {
                if (j < 0 || j >= marked[0].length) continue;
                if (!marked[i][j]) {
                    char c = board.getLetter(i, j);
                    OptimizedTrie.Node next = node.next[c - 'A'];
                    if (c == 'Q' && next != null) next = next.next['U' - 'A'];
                    dfs(board, i, j, next, marked, set);
                }
            }
        }
        marked[row][col] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!trie.contains(word)) return 0;
        if (word.length() < 6) {
            if (word.length() == 5) return 2;
            if (word.length() < 3) return 0;
            return 1;
        }
        else {
            if (word.length() > 7) return 11;
            if (word.length() == 7) return 5;
            return 3;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            board = new BoggleBoard();
            score = 0;
            for (String word : solver.getAllValidWords(board)) {
                StdOut.println(word);
                score += solver.scoreOf(word);
            }
            StdOut.println("Score = " + score);
        }
        final long endTime = System.currentTimeMillis();

        System.out.println("Total execution time: " + (endTime - startTime) );
    }
}

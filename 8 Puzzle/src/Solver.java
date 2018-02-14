/******************************************************************************
 *
 *  Author: Dennis Tran
 *  Written 8/26/17
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private boolean solvable;
    private Node solved;
    private Board initial;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Null argument");
        MinPQ<Node> pq = new MinPQ<Node>();
        MinPQ<Node> twin = new MinPQ<Node>();
        this.initial = initial;
        pq.insert(new Node(initial, null));
        twin.insert(new Node(initial.twin(), null));

        // Loop until found solution
        while (true) {
            Node node = pq.delMin();
            Board board = node.board();
            if (board.isGoal()) {
                solvable = true;
                solved = node;
                break;
            }
            for (Board neighbor : board.neighbors()) {
                Board prev = null;
                if (node.prev != null) prev = node.prev.board;
                if (!neighbor.equals(prev)) {
                    pq.insert(new Node(neighbor, node));
                }
            }

            node = twin.delMin();
            board = node.board();
            if (board.isGoal()) {
                solvable = false;
                break;
            }
            for (Board neighbor : board.neighbors()) {
                Board prev = null;
                if (node.prev != null) prev = node.prev.board;
                if (!neighbor.equals(prev)) {
                    twin.insert(new Node(neighbor, node));
                }
            }
        }
    }

    private class Node implements Comparable<Node>{
        private final Board board;
        private final int priority;
        private final int moves;
        private final Node prev;

        public Node(Board board, Node prev) {
            this.board = board;
            this.prev = prev;
            if (prev == null) {
                moves = 0;
            }
            else {
                moves = prev.moves + 1;
            }
            priority = board.manhattan() + moves;
        }

        public int compareTo(Node that) {
            return Integer.compare(this.priority, that.priority);
        }

        public Board board() {
            return board;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solvable) return solved.moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        Stack<Board> stack = new Stack<>();
        Node temp = solved;
        while (temp.board != initial) {
            stack.push(temp.board);
            temp = temp.prev;
        }
        stack.push(temp.board);
        return stack;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
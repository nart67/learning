/******************************************************************************
 *
 *  Author: Dennis Tran
 *  Written 8/26/17
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int[][] board;
    private final int n;
    private int hamming;
    private int manhattan;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        board = new int[n][n];
        hamming = 0;
        manhattan = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int cur = blocks[i][j];
                board[i][j] = cur;

                // Calculate distance to correct position for each tile
                if (cur != 0) {
                    manhattan += Math.abs(i - ((cur - 1) / n));
                    manhattan += Math.abs(j - ((cur - 1) % n));
                }
            }
        }

        // Loop through board checking all tiles
        int x = 0;
        int y = 0;
        for (int i = 0; i < n * n - 1; i++) {
            // Check if correct tile, otherwise increase hamming
            if (board[y][x] != i + 1) hamming++;

            // Move to next tile
            x++;
            if (x == n) {
                x = 0;
                y++;
            }
        }
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = board[i][j];
            }
        }

        int temp;
        if (tiles[0][0] != 0) {
            if (tiles[0][1] != 0) {
                temp = tiles[0][0];
                tiles[0][0] = tiles[0][1];
                tiles[0][1] = temp;
            }
            else {
                temp = tiles[0][0];
                tiles[0][0] = tiles[1][0];
                tiles[1][0] = temp;
            }
        }
        else {
            temp = tiles[0][1];
            tiles[0][1] = tiles[1][0];
            tiles[1][0] = temp;
        }

        return new Board(tiles);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();
        int[][] tiles = new int[n][n];

        int x = 0;
        int y = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = board[i][j];
                if (board[i][j] == 0) {
                    x = j;
                    y = i;
                }
            }
        }
        if (x != 0) {
            tiles[y][x] = tiles[y][x-1];
            tiles[y][x-1] = 0;
            stack.push(new Board(tiles));
            tiles[y][x-1] = tiles[y][x];
            tiles[y][x] = 0;
        }

        if (x != n - 1) {
            tiles[y][x] = tiles[y][x+1];
            tiles[y][x+1] = 0;
            stack.push(new Board(tiles));
            tiles[y][x+1] = tiles[y][x];
            tiles[y][x] = 0;
        }

        if (y != 0) {
            tiles[y][x] = tiles[y-1][x];
            tiles[y-1][x] = 0;
            stack.push(new Board(tiles));
            tiles[y-1][x] = tiles[y][x];
            tiles[y][x] = 0;
        }

        if (y != n - 1) {
            tiles[y][x] = tiles[y+1][x];
            tiles[y+1][x] = 0;
            stack.push(new Board(tiles));
            tiles[y+1][x] = tiles[y][x];
            tiles[y][x] = 0;
        }
        return stack;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /* // unit tests (not graded)
    public static void main(String[] args) {
        int[][] blocks = {{8, 7, 6}, {5, 4, 3}, {2, 1, 0}};
        Board board = new Board(blocks);
        StdOut.println(board.toString());
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor.toString());
        }
        Board twin = board.twin();
        StdOut.println(twin.toString());
        int[][] blocks2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        board = new Board(blocks2);
        StdOut.println(board.toString());
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
    } */
}
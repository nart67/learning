import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private int top;
    private boolean[] arrOpen;
    private WeightedQuickUnionUF uf;
    private int openSites;
    private boolean[] connBottom;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) throw new java.lang.IllegalArgumentException("n must be > 0");
        this.n = n;
        top = n * n;
        arrOpen = new boolean[n*n];
        uf = new WeightedQuickUnionUF(n*n+1);
        openSites = 0;
        connBottom = new boolean[n*n+1];
    }

    // Convert 2D dimensions to 1D
    private int xyTo1D(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new java.lang.IllegalArgumentException("Site out of bounds");
        }
        return (row - 1) * n + col - 1;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        int pos = xyTo1D(row, col);
        if (arrOpen[pos]) return;
        arrOpen[pos] = true;
        openSites++;
        boolean bottom = false;
        // Connect left if open
        if (col > 1 && arrOpen[pos - 1]) {
            if (!bottom){
                if (connBottom[uf.find(pos - 1)]) bottom = true;
            }
            uf.union(pos, pos - 1);
        }
        // Connect right if open
        if (col < n && arrOpen[pos + 1]) {
            if (!bottom){
                if (connBottom[uf.find(pos + 1)]) bottom = true;
            }
            uf.union(pos, pos + 1);
        }
        // Connect up if open
        if (row > 1 && arrOpen[pos - n]) {
            if (!bottom){
                if (connBottom[uf.find(pos - n)]) bottom = true;
            }
            uf.union(pos, pos - n);
        }
        // Connect down if open
        if (row < n && arrOpen[pos + n]) {
            if (!bottom){
                if (connBottom[uf.find(pos + n)]) bottom = true;
            }
            uf.union(pos, pos + n);
        }
        // If top row, connect to top
        if (row == 1) {
            uf.union(pos, top);
        }
        // If bottom row, connect to bottom
        if (!bottom && row == n) bottom = true;
        if (bottom) connBottom[uf.find(pos)] = true;
    }
    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        return arrOpen[xyTo1D(row, col)];
    }
    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        return uf.connected(xyTo1D(row, col), top);
    }
    // number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return connBottom[uf.find(top)];
    }

}

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double lo;
    private double hi;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException("n and trials must be > 0");
        }
        double[] stats = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
            }
            stats[i] = (double) p.numberOfOpenSites() / (n*n);
        }
        mean = StdStats.mean(stats);
        if (trials == 1) stddev = Double.NaN;
        else stddev = StdStats.stddev(stats);
        lo = mean - 1.96 * stddev / Math.sqrt(trials);
        hi = mean + 1.96 * stddev / Math.sqrt(trials);
    }
    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return lo;
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return hi;
    }
    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = [" + ps.confidenceLo() +
        ", " + ps.confidenceHi() + "]");
    }
}
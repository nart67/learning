/******************************************************************************
 * Author: Dennis Tran
 * Shortest ancestral path implementation
 * Written: 10/28/17
 * For Coursera/Princeton Algorithms 2
 ******************************************************************************/
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new java.lang.IllegalArgumentException("Null argument");
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) {
            throw new java.lang.IndexOutOfBoundsException("Index out of bounds");
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return returnMin(bfsV, bfsW);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) {
            throw new java.lang.IndexOutOfBoundsException("Index out of bounds");
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return returnAncestor(bfsV, bfsW);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new NullPointerException("Null argument");
        for (int i : v) {
            if (i < 0 || i >= G.V()) throw new java.lang.IndexOutOfBoundsException("Index out of bounds");
        }
        for (int i : w) {
            if (i < 0 || i >= G.V()) throw new java.lang.IndexOutOfBoundsException("Index out of bounds");
        }
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return returnMin(bfsV, bfsW);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new NullPointerException("Null argument");
        for (int i : v) {
            if (i < 0 || i >= G.V()) throw new java.lang.IndexOutOfBoundsException("Index out of bounds");
        }
        for (int i : w) {
            if (i < 0 || i >= G.V()) throw new java.lang.IndexOutOfBoundsException("Index out of bounds");
        }
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return returnAncestor(bfsV, bfsW);
    }

    private int returnMin(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {
        int min = Integer.MAX_VALUE;
        int cur;
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                cur = bfsV.distTo(i) + bfsW.distTo(i);
                if (cur < min) min = cur;
            }
        }

        if (min == Integer.MAX_VALUE) return -1;
        return min;
    }

    private int returnAncestor(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {
        int min = Integer.MAX_VALUE;
        int cur;
        int ancestor = 0;
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                cur = bfsV.distTo(i) + bfsW.distTo(i);
                if (cur < min) {
                    min = cur;
                    ancestor = i;
                }
            }
        }

        if (min == Integer.MAX_VALUE) return -1;
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
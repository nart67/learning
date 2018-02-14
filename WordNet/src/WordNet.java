/******************************************************************************
 * Author: Dennis Tran
 * WordNet implementation
 * Written: 10/28/17
 * For Coursera/Princeton Algorithms 2
 ******************************************************************************/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.LinkedList;

public class WordNet {
    private Digraph G;
    private SeparateChainingHashST<String, LinkedList<Integer>> ST;
    private SeparateChainingHashST<Integer, String> nouns;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.IllegalArgumentException("Null argument");
        }

        ST = new SeparateChainingHashST<>();
        nouns = new SeparateChainingHashST<>();

        // Read synsets, save string in ST, then initialize graph with vertices
        In in = new In(synsets);
        String[] split;
        int size = 0;
        String[] split2;
        while (!in.isEmpty()) {
            split = in.readLine().split(",");
            split2 = split[1].split(" ");
            for (int i = 0; i < split2.length; i++) {
                addNoun(split2[i], Integer.parseInt(split[0]));
            }
            nouns.put(Integer.parseInt(split[0]), split[1]);
            size++;
        }
        G = new Digraph(size);


        // Read hypernyms, add edges to graph
        in = new In(hypernyms);
        int v;
        int w;
        while (!in.isEmpty()) {
            split = in.readLine().split(",");
            v = Integer.parseInt(split[0]);
            for (int i = 1; i < split.length; i++) {
                w = Integer.parseInt(split[i]);
                G.addEdge(v, w);
            }
        }

        Topological top = new Topological(G);
        if (!top.hasOrder()) throw new java.lang.IllegalArgumentException("Not DAG");
        int source = -1;
        Iterator<Integer> iter = top.order().iterator();
        while (iter.hasNext()) {
            source = iter.next();
        }
        DirectedDFS dfs = new DirectedDFS(G.reverse(), source);
        if (dfs.count() != size) throw new java.lang.IllegalArgumentException("Not rooted");

        sap = new SAP(G);
    }

    // Add noun to symbol table. If already exists, add to list of integers
    private void addNoun(String noun, int i) {
        LinkedList<Integer> list;
        if (ST.contains(noun)) {
            list = ST.get(noun);
        }
        else {
            list = new LinkedList<>();
        }
        list.add(i);
        ST.put(noun, list);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return ST.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new java.lang.IllegalArgumentException("Null argument");
        return ST.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.IllegalArgumentException("Null argument");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException("Not WordNet noun");
        }
        Iterable<Integer> v = ST.get(nounA);
        Iterable<Integer> w = ST.get(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.IllegalArgumentException("Null argument");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException("Not WordNet noun");
        }
        Iterable<Integer> v = ST.get(nounA);
        Iterable<Integer> w = ST.get(nounB);
        return nouns.get(sap.ancestor(v, w));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        for (String s : wordnet.nouns()) {
            StdOut.println(s);
        }
        StdOut.println(wordnet.ST.size());
        StdOut.println(wordnet.G.V());
        StdOut.println(wordnet.G.E());
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int length   = wordnet.distance(v, w);
            String ancestor = wordnet.sap(v, w);
            StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
        }
    }
}
/******************************************************************************
 * Author: Dennis Tran
 * Outcast implementation
 * Written: 10/28/17
 * For Coursera/Princeton Algorithms 2
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int max = Integer.MIN_VALUE;
        int noun = 0;
        int distance;
        int[] cache = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = i + 1; j < nouns.length; j++) {
                distance = wordnet.distance(nouns[i], nouns[j]);
                cache[i] += distance;
                cache[j] += distance;
            }
            if (cache[i] > max) {
                max = cache[i];
                noun = i;
            }
        }
        return nouns[noun];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
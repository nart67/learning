/******************************************************************************
 * Author: Dennis Tran
 * Burrows Wheeler implementation
 * Written: 11/24/17
 * For Coursera/Princeton Algorithms 2
 ******************************************************************************/
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffix = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            if (suffix.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < s.length(); i++) {
            int t = suffix.index(i) - 1;
            if (t < 0) BinaryStdOut.write(s.charAt(s.length() - 1));
            else BinaryStdOut.write(s.charAt(t));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] t = BinaryStdIn.readString().toCharArray();

        // sort by key-indexed counting on dth character
        // based on LSD.java from https://algs4.cs.princeton.edu/

        char[] sorted = new char[t.length];
        int[] next = new int[t.length];
        // compute frequency counts
        int[] count = new int[R+1];
        for (int i = 0; i < t.length; i++)
            count[t[i] + 1]++;

        // compute cumulates
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];

        // move data
        for (int i = 0; i < t.length; i++) {
            sorted[count[t[i]]] = t[i];
            next[count[t[i]]++] = i;
        }

        for (int i = 0; i < next.length; i++) {
            BinaryStdOut.write(sorted[first]);
            first = next[first];
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}

/******************************************************************************
 * Author: Dennis Tran
 * Circular suffix array data type
 * Written: 11/24/17
 * For Coursera/Princeton Algorithms 2
 ******************************************************************************/
import edu.princeton.cs.algs4.In;

import java.util.Arrays;

public class CircularSuffixArray {
    private char[] chars;
    private int length;
    private Suffix[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new java.lang.IllegalArgumentException("Null string");
        chars = s.toCharArray();

        length = chars.length;
        index = new Suffix[length];
        for (int i = 0; i < length; i++) {
            index[i] = new Suffix(chars, i);
        }
        Arrays.sort(index);
    }

    // implementation of suffixes
    private class Suffix implements Comparable<Suffix> {
        char[] chars;
        int length;
        int offset;

        public Suffix(char[] chars, int offset) {
            this.chars = chars;
            length = chars.length;
            this.offset = offset;
        }

        public char charAt(int i) {
            if (i >= length || i < 0) throw new IllegalArgumentException("Argument out of range");
            if (offset + i >= length) return chars[offset + i - length];
            return chars[offset + i];
        }

        public int offset() {
            return offset;
        }

        public int compareTo(Suffix that) {
            if (that == null) throw new IllegalArgumentException("Null argument");
            for (int i = 0; i < length; i++) {
                if (charAt(i) < that.charAt(i)) return -1;
                if (charAt(i) > that.charAt(i)) return 1;
            }
            return 0;
        }
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i >= length || i < 0) throw new IllegalArgumentException("Argument out of range");

        return index[i].offset();
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        In in = new In(args[0]);
        String s = in.readAll();
        in.close();
        CircularSuffixArray suffix = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            System.out.println(suffix.index(i));
        }
    }
}

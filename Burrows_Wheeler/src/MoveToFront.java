/******************************************************************************
 * Author: Dennis Tran
 * Move to Front implementation
 * Written: 11/25/17
 * For Coursera/Princeton Algorithms 2
 ******************************************************************************/
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static int R = 256;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
       char[] chars = new char[R];
        for (int i = 0; i < R; i++) {
            chars[i] = (char) i;
        }

        String s = BinaryStdIn.readString();
        for (int i = 0; i < s.length(); i++) {
            int index = indexOf(chars, s.charAt(i));
            char c = chars[index];
            BinaryStdOut.write((char) index);
            for (int j = index; j > 0; j--)
                chars[j] = chars[j-1];
            chars[0] = c;
        }

        BinaryStdOut.close();
    }

    private static int indexOf(char[] chars, char c) {
        for (int i = 0; i < chars.length; i++) {
            if (c == chars[i]) return i;
        }

        return -1;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chars = new char[R];
        for (int i = 0; i < R; i++) {
            chars[i] = (char) i;
        }

        String s = BinaryStdIn.readString();
        for (int i = 0; i < s.length(); i++) {
            int index = s.charAt(i);
            char c = chars[index];
            BinaryStdOut.write(c);
            for (int j = index; j > 0; j--)
                chars[j] = chars[j-1];
            chars[0] = c;
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}

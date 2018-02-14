/******************************************************************************
 * Author: Dennis Tran
 * Permutation implementation
 * Written: 8/13/17
 ******************************************************************************/
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> random = new RandomizedQueue<String>();
        int i = 0;

        if (k > 0) {
            while (!StdIn.isEmpty()) {
                int j;
                String item = StdIn.readString();
                if (i < k) random.enqueue(item);
                else {
                    j = StdRandom.uniform(0, i+1);
                    if (j < k) {
                        random.dequeue();
                        random.enqueue(item);
                    }
                }
                i++;
            }
        }

        for (i = 0; i < k; i++) {
            StdOut.println(random.dequeue());
        }
    }
}

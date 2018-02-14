/******************************************************************************
 * Author: Dennis Tran
 * Randomized queue implementation
 * Written: 8/13/17
 * Based on http://algs4.cs.princeton.edu/13stacks/ResizingArrayStack.java.html
 ******************************************************************************/
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;         // array of items
    private int n;            // number of elements on stack

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the queue
    public int size() {
        return n;
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;

        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException("Item cannot be null");
        if (n == a.length) resize(2*a.length);    // double size of array if necessary
        a[n++] = item;                                    // add item
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int i = StdRandom.uniform(0, n);          // generate random index
        Item item = a[i];                           // save item to return
        a[i] = a[n-1];                              // move last item to fill in gap
        a[n-1] = null;                              // to avoid loitering
        n--;
        // shrink size of array if necessary
        if (n > 0 && n == a.length/4) resize(a.length/2);
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return a[StdRandom.uniform(0, n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }

    private class RandomArrayIterator implements Iterator<Item> {
        private int i;
        private Item[] ita = (Item[]) new Object[n];

        public RandomArrayIterator() {
            for (int i = 0; i < n; i++) {
                ita[i] = a[i];
            }
            randomize();
            i = n-1;
        }

        public boolean hasNext() {
            return i >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return ita[i--];
        }

        // Use Fisher–Yates shuffle to randomize iterators
        private void randomize() {
            int j;
            Item temp;
            for (int i = 0; i < n - 1; i++) {
                j = StdRandom.uniform(i, n);
                temp = ita[i];
                ita[i] = ita[j];
                ita[j] = temp;
            }
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<Integer> random = new RandomizedQueue<Integer>();
        for (int i = 1; i < 11; i++) {
            random.enqueue(i);
        }
        Iterator<Integer> it1 = random.iterator();
        Iterator<Integer> it2 = random.iterator();
        while (it1.hasNext()) {
            int i = it1.next();
            StdOut.println(i);
        }
        StdOut.println("end");
        while (it2.hasNext()) {
            int i = it2.next();
            StdOut.println(i);
        }
    }
}
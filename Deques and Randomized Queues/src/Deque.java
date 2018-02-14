/******************************************************************************
 * Author: Dennis Tran
 * Deque implementation
 * Written: 8/13/17
 * Based on http://algs4.cs.princeton.edu/13stacks/LinkedStack.java.html
 ******************************************************************************/
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private int n;          // size of the deque
    private Node first;     // front of deque
    private Node last;      // end of deque

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
        assert check();
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException("Item cannot be null");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.previous = null;
        if (first.next != null) first.next.previous = first;
        n++;
        if (n <= 1) last = first;
        assert check();
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException("Item cannot be null");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.previous = oldlast;
        if (last.previous != null) last.previous.next = last;
        n++;
        if (n == 1) first = last;
        assert check();
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = first.item;        // save item to return
        first = first.next;            // delete first node
        n--;
        if (n > 0) first.previous = null;
        if (n <= 1) last = first;
        assert check();
        return item;                   // return the saved item
    }
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = last.item;         // save item to return
        last = last.previous;          // delete first node
        n--;
        if (n > 0) last.next = null;
        if (n <= 1) first = last;
        assert check();
        return item;                   // return the saved item
    }
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // check internal invariants
    private boolean check() {

        // check a few properties of instance variable 'first'
        if (n < 0) {
            return false;
        }
        if (n == 0) {
            if (first != null) return false;
        }
        else if (n == 1) {
            if (first == null)      return false;
            if (first.next != null) return false;
        }
        else {
            if (first == null)      return false;
            if (first.next == null) return false;
        }

        // check internal consistency of instance variable n
        int numberOfNodes = 0;
        for (Node x = first; x != null && numberOfNodes <= n; x = x.next) {
            numberOfNodes++;
        }
        if (numberOfNodes != n) return false;

        return true;
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
        StdOut.println(deque.isEmpty());
        deque.removeLast();
        StdOut.println(deque.isEmpty());
        deque.addLast(2);
        StdOut.println(deque.isEmpty());
        deque.removeLast();
        StdOut.println(deque.isEmpty());
        deque.addFirst(1);
        StdOut.println(deque.isEmpty());
        deque.removeFirst();
        StdOut.println(deque.isEmpty());
        deque.addLast(2);
        StdOut.println(deque.isEmpty());
        deque.removeFirst();
        StdOut.println(deque.isEmpty());
        for (Integer i : deque)
            StdOut.println(i);
    }
}
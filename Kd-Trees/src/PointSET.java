import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    private final SET<Point2D> bst;

    // construct an empty set of points
    public PointSET() {
        bst = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return bst.isEmpty();
    }
    // number of points in the set
    public int size() {
        return bst.size();
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument");
        if (bst.contains(p)) return;
        bst.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument");
        return bst.contains(p);
    }
    // draw all points to standard draw
    public void draw() {
        for (Point2D point : bst) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Null argument");
        Stack<Point2D> stack = new Stack<>();
        for (Point2D point : bst) {
            if (rect.contains(point)) stack.push(point);
        }
        return stack;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument");
        if (isEmpty()) return null;
        Point2D closest = null;
        for (Point2D point : bst) {
            if (closest == null) closest = point;
            if (point.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) {
                closest = point;
            }
        }

        return closest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // todo
    }
}
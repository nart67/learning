import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class KdTree {
    private static final boolean X_AXIS = false;
    private Node root;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
    }

    private static class Node {
        private final Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, boolean xy, Double min, Double max) {
            if (p == null) throw new IllegalArgumentException("Null argument");
            this.p = p;
            if (xy) rect = new RectHV(min, p.y(), max, p.y());
            else rect = new RectHV(p.x(), min, p.x(), max);
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }
    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument");
        if (contains(p)) return;
        size++;
        root = put(root, p, X_AXIS, 0.0, 0.0, 1.0, 1.0);
    }

    // insert the Node in the subtree rooted at h
    private Node put(Node h, Point2D p, boolean xy,
                     double xmin, double ymin, double xmax, double ymax) {

        if (h == null) {
            double min, max;

            if (xy) {
                min = xmin;
                max = xmax;
            }
            else {
                min = ymin;
                max = ymax;
            }
            return new Node(p, xy, min, max);
        }

        // if xy is true, compare y.
        int cmp;
        if (xy) cmp = Double.compare(p.y(), h.p.y());
        else cmp = Double.compare(p.x(), h.p.x());

        if (cmp < 0) {
            if (xy) ymax = h.p.y();
            else xmax = h.p.x();
            h.lb = put(h.lb, p, !xy, xmin, ymin, xmax, ymax);
        }
        else {
            if (xy) ymin = h.p.y();
            else xmin = h.p.x();
            h.rt = put(h.rt, p, !xy, xmin, ymin, xmax, ymax);
        }

        return h;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument");
        return get(p) != null;
    }

    private Node get(Point2D p) {
        return get(root, p, X_AXIS);
    }

    // Node associated with the given point in subtree rooted at x; null if no such key
    private Node get(Node x, Point2D p, boolean xy) {
        while (x != null) {
            if (p.equals(x.p)) return x;
            int cmp;
            if (xy) cmp = Double.compare(p.y(), x.p.y());
            else cmp = Double.compare(p.x(), x.p.x());

            if (cmp < 0) x = get(x.lb, p, !xy);
            else x = get(x.rt, p, !xy);
        }
        return null;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, X_AXIS);
    }

    private void draw(Node h, boolean xy) {
        if (h == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        h.p.draw();
        if (xy) StdDraw.setPenColor(StdDraw.BLUE);
        else StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        h.rect.draw();

        draw(h.lb, !xy);
        draw(h.rt, !xy);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Null argument");
        Stack<Point2D> stack = new Stack<>();
        inRect(root, rect, X_AXIS, stack);
        return stack;
    }

    private void inRect(Node h, RectHV rect, boolean xy, Stack<Point2D> stack) {
        if (h == null) return;
        if (rect.contains(h.p)) stack.push(h.p);

        if (xy) {
            if (rect.ymin() <= h.rect.ymin()) {
                inRect(h.lb, rect, !xy, stack);
            }
            if (rect.ymax() >= h.rect.ymax()) {
                inRect(h.rt, rect, !xy, stack);
            }
        } else {
            if (rect.xmin() <= h.rect.xmin()) {
                inRect(h.lb, rect, !xy, stack);
            }
            if (rect.xmax() >= h.rect.xmax()) {
                inRect(h.rt, rect, !xy, stack);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument");
        if (isEmpty()) return null;

        return nearest(root, p, root.p, X_AXIS);
    }

    // nearest neighbor in subtree h
    private Point2D nearest(Node h, Point2D p, Point2D closest, boolean xy) {
        if (h == null) return closest;

        if (h.p.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) {
            closest = h.p;
        }

        if (p.equals(closest)) return closest;

        // if xy is true, compare y.
        int cmp;
        if (xy) cmp = Double.compare(p.y(), h.p.y());
        else cmp = Double.compare(p.x(), h.p.x());

        if (cmp < 0) {
            closest = nearest(h.lb, p, closest, !xy);
            if (h.rt == null) return closest;
            if (closest.distanceSquaredTo(p) > h.rect.distanceSquaredTo(p)) {
                closest = nearest(h.rt, p, closest, !xy);
            }
        }
        else {
            closest = nearest(h.rt, p, closest, !xy);
            if (h.lb == null) return closest;
            if (closest.distanceSquaredTo(p) > h.rect.distanceSquaredTo(p)) {
                closest = nearest(h.lb, p, closest, !xy);
            }
        }

        return closest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        int n = Integer.parseInt(args[0]);
        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            tree.insert(new Point2D(x, y));
        }

        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            System.out.println(tree.nearest(new Point2D(x, y)));
        }

        RectHV rect = new RectHV(.2, .1, .5, .5);
        for (Point2D p : tree.range(rect)) {
            System.out.println(p.toString());
        }
        System.out.println(tree.nearest(new Point2D(.1, .11)));
    }
}
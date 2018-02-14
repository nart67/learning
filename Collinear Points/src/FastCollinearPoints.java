/******************************************************************************
 *
 *  Author: Dennis Tran
 *  Written 8/20/17
 *
 ******************************************************************************/
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;
    private int n;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.IllegalArgumentException("Invalid argument");
        validate(points);

        Point[] sorted = Arrays.copyOf(points, points.length);
        segments = new LineSegment[1];
        n = 0;

        Arrays.sort(sorted);
        for (int i = 0; i < sorted.length; i++) {
            Point[] byslopes = new Point[sorted.length];

            for (int j = 0; j < sorted.length; j++) {
                byslopes[j] = sorted[j];
            }

            Arrays.sort(byslopes, sorted[i].slopeOrder());
            int repeat = 0;  // counter for repeated slopes in array
            boolean small = true;  // only keep segment if point at i is smallest point
            for (int j = 0; j < byslopes.length; j++) {
                double next;
                if (j == byslopes.length - 1) next = Double.NaN;
                else next = sorted[i].slopeTo(byslopes[j + 1]);
                if (sorted[i].slopeTo(byslopes[j]) == next) {
                    if (small) {
                        if (sorted[i].compareTo(byslopes[j]) > 0) small = false;
                        repeat++;
                    }
                }
                else {
                    if (repeat >= 2 && small) {
                        segments[n] = new LineSegment(sorted[i], byslopes[j]);
                        n++;
                        if (segments.length == n) resize(2 * n);
                    }
                    small = true;
                    repeat = 0;
                }
            }
        }
        resize(n);
    }
    // the number of line segments
    public int numberOfSegments() {
        return n;
    }
    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;

        // textbook implementation
        LineSegment[] temp = new LineSegment[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = segments[i];
        }
        segments = temp;
    }

    // Check input for errors
    private void validate(Point[] repeats) {
        for (int i = 0; i < repeats.length; i++) {
            if (repeats[i] == null) throw new java.lang.IllegalArgumentException("Null point");
            for (int j = i + 1; j < repeats.length; j++) {
                if (repeats[j] == null) throw new java.lang.IllegalArgumentException("Null point");
                if (repeats[i].compareTo(repeats[j]) == 0) {
                    throw new java.lang.IllegalArgumentException("Repeated point");
                }
            }
        }
    }
}
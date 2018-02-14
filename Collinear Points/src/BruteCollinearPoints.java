/******************************************************************************
 *
 *  Author: Dennis Tran
 *  Written 8/20/17
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int n;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.IllegalArgumentException("Invalid argument");
        validate(points);

        segments = new LineSegment[1];
        n = 0;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        if (points[i].slopeTo(points[k]) != points[i].slopeTo(points[j])) break;
                        Point[] check = { points[i], points[j], points[k], points[l] };
                        if (isCollinear(check)) {
                            segments[n] = new LineSegment(check[0], check[3]);
                            n++;
                            if (segments.length == n) resize(2*n);
                        }
                    }
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

    private boolean isCollinear(Point[] check) {
        Arrays.sort(check);
        if (check[0].slopeTo(check[1]) == check[0].slopeTo(check[2])) {
            if (check[0].slopeTo(check[1]) == check[0].slopeTo(check[3])) {
                return true;
            }
        }
        return false;
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
}

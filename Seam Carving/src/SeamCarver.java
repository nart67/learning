/******************************************************************************
 * Author: Dennis Tran
 * Seam Carver implementation
 * Written: 11/05/17
 * For Coursera/Princeton Algorithms 2
 ******************************************************************************/
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private int height;
    private int width;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new java.lang.IllegalArgumentException("Null argument");
        this.picture = new Picture(picture);
        height = picture.height();
        width = picture.width();
    }

    // current picture
    public Picture picture() {
        Picture current = new Picture(width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                current.set(j, i, picture.get(j, i));
            }
        }
        picture = current;
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= width() || y >= height() ||
                x < 0 || y < 0) throw new java.lang.IllegalArgumentException("Out of range");
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) return 1000;

        return Math.sqrt(energyX(x, y) + energyY(x, y));
    }

    private double energyX(int x, int y) {
        int color_left = picture.getRGB(x - 1, y);
        int color_right = picture.getRGB(x + 1, y);
        int blue = (color_left & 255) - (color_right & 255);
        int green = ((color_left >> 8) & 255) - ((color_right >> 8) & 255);
        int red = ((color_left >> 16) & 255) - ((color_right >> 16) & 255);
        return blue * blue + green * green + red * red;
    }

    private double energyY(int x, int y) {
        int color_down = picture.getRGB(x, y - 1);
        int color_up = picture.getRGB(x, y + 1);
        int blue = (color_down & 255) - (color_up & 255);
        int green = ((color_down >> 8) & 255) - ((color_up >> 8) & 255);
        int red = ((color_down >> 16) & 255) - ((color_up >> 16) & 255);
        return blue * blue + green * green + red * red;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture temp = picture;
        int holder = height;
        height = width;
        width = holder;
        picture = new Picture(width(), height());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                picture.setRGB(j, i, temp.getRGB(i, j));
            }
        }
        int[] result = findVerticalSeam();
        picture = temp;
        width = height;
        height = holder;
        return result;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energies = new double[height()][width()];
        double[][] distTo = new double[height()][width()];
        int[][] indexTo = new int[height()][width()];
        for (int i = 0; i < energies.length; i++) {
            for (int j = 0; j < energies[0].length; j++) {
                energies[i][j] = energy(j, i);
                if (i == 0) distTo[0][j] = 1000;
                else distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int i = 0; i < energies.length - 1; i++) {
            for (int j = 0; j < energies[0].length; j++) {
                for (int k = -1; k < 2; k++) {
                    relaxTo(energies, distTo, indexTo, i, j, j + k);
                }
            }
        }

        double min_seam = Integer.MAX_VALUE;
        int index_last = 0;
        for (int j = 0; j < energies[0].length; j++) {
            if (distTo[height()-1][j] < min_seam) {
                min_seam = distTo[height()-1][j];
                index_last = j;
            }
        }

        int[] result = new int[height()];
        result[height() - 1] = index_last;
        for (int i = height() - 1; i > 0; i--) {
            result[i-1] = indexTo[i][result[i]];
        }
        return result;
    }

    private void relaxTo(double[][] energies, double[][] distTo, int[][] indexTo,
                           int i, int j, int to) {
        if (i < 0 || to < 0 || i >= energies.length || to >= energies[0].length) return;
        if (distTo[i][j] + energies[i+1][to] < distTo[i+1][to]) {
            indexTo[i+1][to] = j;
            distTo[i+1][to] = distTo[i][j] + energies[i+1][to];
        }
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new java.lang.IllegalArgumentException("Null seam");
        if (seam.length != width()) throw new java.lang.IllegalArgumentException("Wrong seam width");
        if (height() <= 1) throw new java.lang.IllegalArgumentException("No seams to remove");
        for (int i = 0; i < seam.length; i++) {
            if (i > 0) {
                if (seam[i] - seam[i-1] > 1 || seam[i] - seam[i-1] < -1 ||
                        seam[i] < 0 || seam[i] >= height()) {
                    throw new java.lang.IllegalArgumentException("Invalid seam");
                }
            }
            for (int j = seam[i]; j < height() - 1; j++) {
                picture.setRGB(i, j, picture.getRGB(i, j + 1));
            }
        }
        height--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new java.lang.IllegalArgumentException("Null seam");
        if (seam.length != height()) throw new java.lang.IllegalArgumentException("Wrong seam height");
        if (width() <= 1) throw new java.lang.IllegalArgumentException("No seams to remove");
        for (int i = 0; i < seam.length; i++) {
            if (i > 0) {
                if (seam[i] - seam[i-1] > 1 || seam[i] - seam[i-1] < -1 ||
                        seam[i] < 0 || seam[i] >= width()) {
                    throw new java.lang.IllegalArgumentException("Invalid seam");
                }
            }
            for (int j = seam[i]; j < width() - 1; j++) {
                picture.setRGB(j, i, picture.getRGB(j + 1, i));
            }
        }
        width--;
    }
}

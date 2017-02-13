import java.awt.Color;
import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private static int BORDER_ENERGY = 1000;
    private Picture picture;
    private double[][] energyMatrix;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.energyMatrix = new double[picture.height()][picture.width()];
        for (int row = 0; row < picture.height(); ++row) {
            for (int col = 0; col < picture.width(); ++col) {
                energyMatrix[row][col] = energy(col, row);
            }
        }
    }

    public Picture picture() {
        // TODO: We need to generate new picture from the current pixel matrix
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }
    public int height() {
        return picture.height();
    }

    // Energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x == 0 || x == picture.width() - 1 || y == 0 || y == picture.height() -1) {
            return BORDER_ENERGY;
        }

        Color pixelLeft = picture.get(x - 1, y);
        Color pixelRight = picture.get(x + 1, y);
        Color pixelUp = picture.get(x, y - 1);
        Color pixelDown = picture.get(x, y + 1);

        double gradientXSquared = getGradientSquared(pixelRight, pixelLeft);
        double gradientYSquared = getGradientSquared(pixelUp, pixelDown);

        return Math.sqrt(gradientXSquared + gradientYSquared);
    }               

    private double getGradientSquared(Color pixel1, Color pixel2) {
        int rDiff = pixel1.getRed() - pixel2.getRed();
        int gDiff = pixel1.getGreen() - pixel2.getGreen();
        int bDiff = pixel1.getBlue() - pixel2.getBlue();

        return Math.pow(rDiff, 2) + Math.pow(gDiff, 2) + Math.pow(bDiff, 2);
    }

    public int[] findHorizontalSeam() {
        return null;
    }

    public int[] findVerticalSeam() {
        double[][] minDistTo = new double[picture.height()][picture.width()];
        Arrays.fill(minDistTo[0], BORDER_ENERGY);
        int[][] minEdgeTo = new int[picture.height()][picture.width()];
        double minTotalEnergy = Double.MAX_VALUE;
        int minPathEndIdx = Integer.MAX_VALUE;

        // Traverse the DAG down row by row and store the lowest energy path and corresponding pixel's position
        for (int row = 1; row < picture.height(); ++row) {
            for (int col = 0; col < picture.width(); ++col) {
                double upperLeft = (col == 0) ? Double.MAX_VALUE : minDistTo[row - 1][col - 1];
                double upperCenter = minDistTo[row - 1][col];
                double upperRight = (col == picture.width() - 1) ? Double.MAX_VALUE : minDistTo[row - 1][col + 1];

                if (upperLeft <= upperCenter && upperLeft <= upperRight) {
                    minDistTo[row][col] = upperLeft + energyMatrix[row][col];
                    minEdgeTo[row][col] = col - 1;
                } else if (upperCenter <= upperLeft && upperCenter <= upperRight) {
                    minDistTo[row][col] = upperCenter + energyMatrix[row][col];
                    minEdgeTo[row][col] = col;
                } else {
                    minDistTo[row][col] = upperRight + energyMatrix[row][col];
                    minEdgeTo[row][col] = col + 1;
                }

                if (row == picture.height() - 1) {
                    if (minDistTo[row][col] < minTotalEnergy) {
                        minTotalEnergy = minDistTo[row][col];
                        minPathEndIdx = col;
                    }
                }
            }
        }

        // Find minimum energy path
        int[] minPath = new int[picture.height()];
        minPath[picture.height() - 1] = minPathEndIdx;
        for (int i = picture.height() - 2; i >= 0; --i) {
            minPath[i] = minEdgeTo[i + 1][minPath[i + 1]];
        }

        return minPath;
    }

    public void removeHorizontalSeam(int[] seam) {

    }

    public void removeVerticalSeam(int[] seam) {

    }
}

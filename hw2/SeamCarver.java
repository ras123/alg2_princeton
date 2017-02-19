import java.awt.Color;
import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private static int BORDER_ENERGY = 1000;
    private Picture picture;
    private int pictureWidth;
    private int pictureHeight;
    private Color[][] colorMatrix;
    private double[][] energyMatrix;
    private boolean isPictureTransposed;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.pictureWidth = picture.width();
        this.pictureHeight = picture.height();
        this.colorMatrix = new Color[pictureHeight][pictureWidth];
        for (int row = 0; row < pictureHeight; ++row) {
            for (int col = 0; col < pictureWidth; ++col) {
                colorMatrix[row][col] = picture.get(col, row);
            }
        }

        this.energyMatrix = new double[pictureHeight][pictureWidth];
        for (int row = 0; row < pictureHeight; ++row) {
            for (int col = 0; col < pictureWidth; ++col) {
                energyMatrix[row][col] = calculateEnergy(row, col);
            }
        }

        this.isPictureTransposed = false;
    }

    public Picture picture() {
        // TODO: We need to generate new picture from the current pixel matrix
        return new Picture(picture);
    }

    public int width() {
        return isPictureTransposed ? pictureHeight : pictureWidth;
    }
    public int height() {
        return isPictureTransposed ? pictureWidth : pictureHeight;
    }

    // Energy of pixel at column x and row y
    public double energy(int x, int y) {
        return isPictureTransposed ? energyMatrix[x][y] : energyMatrix[y][x];
    }

    private double calculateEnergy(int row, int col) {
        if (col == 0 || col == pictureWidth - 1 || row == 0 || row == pictureHeight - 1) {
            return BORDER_ENERGY;
        }

        Color pixelLeft = colorMatrix[row][col - 1];
        Color pixelRight = colorMatrix[row][col + 1];
        Color pixelUp = colorMatrix[row - 1][col];
        Color pixelDown = colorMatrix[row + 1][col];

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
        if (!isPictureTransposed) {
            transposeEnergyMatrix();
        }

        return findSeam();
    }

    public int[] findVerticalSeam() {
        if (isPictureTransposed) {
            transposeEnergyMatrix();
        }

        return findSeam();
    }

    private void transposeEnergyMatrix() {
        double[][] energyMatrixTransposed = new double[pictureWidth][pictureHeight];
        for (int row = 0; row < pictureHeight; ++row) {
            for (int col = 0; col < pictureWidth; ++col) {
                energyMatrixTransposed[col][row] = energyMatrix[row][col];
            }
        }

        energyMatrix = energyMatrixTransposed;

        int tmp = pictureWidth;
        pictureWidth = pictureHeight;
        pictureHeight = tmp;

        isPictureTransposed = !isPictureTransposed;
    }

    private int[] findSeam() {
        double[][] minDistTo = new double[pictureHeight][pictureWidth];
        Arrays.fill(minDistTo[0], BORDER_ENERGY);
        int[][] minPixelTo = new int[pictureHeight][pictureWidth];
        double minTotalEnergy = Double.MAX_VALUE;
        int minPathEndIdx = Integer.MAX_VALUE;

        // Traverse the DAG down row by row and store the lowest energy path and corresponding pixel's position
        for (int row = 1; row < pictureHeight; ++row) {
            for (int col = 0; col < pictureWidth; ++col) {
                double upperLeft = (col == 0) ? Double.MAX_VALUE : minDistTo[row - 1][col - 1];
                double upperCenter = minDistTo[row - 1][col];
                double upperRight = (col == pictureWidth - 1) ? Double.MAX_VALUE : minDistTo[row - 1][col + 1];

                if (upperLeft <= upperCenter && upperLeft <= upperRight) {
                    minDistTo[row][col] = upperLeft + energyMatrix[row][col];
                    minPixelTo[row][col] = col - 1;
                } else if (upperCenter <= upperLeft && upperCenter <= upperRight) {
                    minDistTo[row][col] = upperCenter + energyMatrix[row][col];
                    minPixelTo[row][col] = col;
                } else {
                    minDistTo[row][col] = upperRight + energyMatrix[row][col];
                    minPixelTo[row][col] = col + 1;
                }

                if (row == pictureHeight - 1) {
                    if (minDistTo[row][col] < minTotalEnergy) {
                        minTotalEnergy = minDistTo[row][col];
                        minPathEndIdx = col;
                    }
                }
            }
        }

        // Reconstruct minimum energy path by following back pixels that got us to the current one
        int[] minPath = new int[pictureHeight];
        minPath[pictureHeight - 1] = minPathEndIdx;
        for (int i = pictureHeight - 2; i >= 0; --i) {
            minPath[i] = minPixelTo[i + 1][minPath[i + 1]];
        }

        return minPath;
    }

    public void removeHorizontalSeam(int[] seam) {
    }

    public void removeVerticalSeam(int[] seam) {
        --pictureWidth;
        for(int i = 0; i < seam.length; ++i) {
            System.arraycopy(colorMatrix[i], seam[i] + 1, colorMatrix[i], seam[i], pictureWidth - seam[i]);
        }

        // Update energy matrix
        for (int row = 0; row < pictureHeight; ++row) {
            for (int col = 0; col < pictureWidth; ++col) {
                energyMatrix[row][col] = energy(col, row);
            }
        }
    }
}

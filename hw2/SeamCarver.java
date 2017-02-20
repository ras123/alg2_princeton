import java.awt.Color;
import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private static final int BORDER_ENERGY = 1000;
    private int pictureWidth;
    private int pictureHeight;
    private Color[][] colorMatrix;
    private double[][] energyMatrix;
    private boolean isPictureTransposed;

    private enum SeamDirection {
        VERTICAL, HORIZONTAL;
    }

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException();
        }

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
        if (isPictureTransposed) {
            transposePicture();
        }

        Picture picture = new Picture(pictureWidth, pictureHeight);
        for (int row = 0; row < pictureHeight; ++row) {
            for (int col = 0; col < pictureWidth; ++col) {
                picture.set(col, row, colorMatrix[row][col]);
            }
        }

        return picture;
    }

    public int width() {
        return isPictureTransposed ? pictureHeight : pictureWidth;
    }
    public int height() {
        return isPictureTransposed ? pictureWidth : pictureHeight;
    }

    // Energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }

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
            transposePicture();
        }

        return findSeam();
    }

    public int[] findVerticalSeam() {
        if (isPictureTransposed) {
            transposePicture();
        }

        return findSeam();
    }

    private void transposePicture() {
        Color[][] colorMatrixTransposed = new Color[pictureWidth][pictureHeight];
        double[][] energyMatrixTransposed = new double[pictureWidth][pictureHeight];
        for (int row = 0; row < pictureHeight; ++row) {
            for (int col = 0; col < pictureWidth; ++col) {
                colorMatrixTransposed[col][row] = colorMatrix[row][col];
                energyMatrixTransposed[col][row] = energyMatrix[row][col];
            }
        }

        colorMatrix = colorMatrixTransposed;
        energyMatrix = energyMatrixTransposed;

        int tmp = pictureWidth;
        pictureWidth = pictureHeight;
        pictureHeight = tmp;

        isPictureTransposed = !isPictureTransposed;
    }

    private int[] findSeam() {
        double[] minDistTo = new double[pictureWidth];
        Arrays.fill(minDistTo, BORDER_ENERGY);
        int[][] minPixelTo = new int[pictureHeight][pictureWidth];
        double previousLeftValue = BORDER_ENERGY;
        double minTotalEnergy = Double.MAX_VALUE;
        int minPathEndIdx = 0;

        // Traverse the DAG down row by row and store the lowest energy path and corresponding pixel's position
        for (int row = 1; row < pictureHeight; ++row) {
            // double previousLeftValue = minDistTo[0];
            for (int col = 0; col < pictureWidth; ++col) {
                double upperLeft = (col == 0) ? Double.MAX_VALUE : previousLeftValue;
                double upperCenter = minDistTo[col];
                double upperRight = (col == pictureWidth - 1) ? Double.MAX_VALUE : minDistTo[col + 1];

                previousLeftValue = minDistTo[col];
                if (upperLeft <= upperCenter && upperLeft <= upperRight) {
                    minDistTo[col] = upperLeft + energyMatrix[row][col];
                    minPixelTo[row][col] = col - 1;
                } else if (upperCenter <= upperLeft && upperCenter <= upperRight) {
                    minDistTo[col] = upperCenter + energyMatrix[row][col];
                    minPixelTo[row][col] = col;
                } else {
                    minDistTo[col] = upperRight + energyMatrix[row][col];
                    minPixelTo[row][col] = col + 1;
                }

                if (row == pictureHeight - 1) {
                    if (minDistTo[col] < minTotalEnergy) {
                        minTotalEnergy = minDistTo[col];
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
        validateSeam(seam, SeamDirection.HORIZONTAL);

        if (!isPictureTransposed) {
            transposePicture();
        }

        removeSeam(seam);
    }

    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, SeamDirection.VERTICAL);

        if (isPictureTransposed) {
            transposePicture();
        }

        removeSeam(seam);
    }

    private void validateSeam(int[] seam, SeamDirection seamDirection) {
        if (seam == null) {
            throw new NullPointerException();
        }

        int size;
        switch (seamDirection) {
            case HORIZONTAL:
                if (height() <= 1 || seam.length != width()) {
                    throw new IllegalArgumentException();
                }
                size = height();
                break;
            case VERTICAL:
                if (width() <= 1 || seam.length != height()) {
                    throw new IllegalArgumentException();
                }
                size = width();
                break;
            default:
                throw new IllegalArgumentException();
        }

        for (int i = 0; i < seam.length; ++i) {
            if (seam[i] < 0 || seam[i] >= size) {
                throw new IllegalArgumentException();
            }

            if (i != seam.length - 1) {
                if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private void removeSeam(int[] seam) {
        --pictureWidth;
        for (int i = 0; i < seam.length; ++i) {
            System.arraycopy(colorMatrix[i], seam[i] + 1, colorMatrix[i], seam[i], pictureWidth - seam[i]);
            System.arraycopy(energyMatrix[i], seam[i] + 1, energyMatrix[i], seam[i], pictureWidth - seam[i]);
        }

        // Update energy matrix
        for (int row = 0; row < pictureHeight; ++row) {
            int col = seam[row];
            if (col != 0) {
                energyMatrix[row][col - 1] = calculateEnergy(row, col - 1);
            }

            if (col != pictureWidth) {
                energyMatrix[row][col] = calculateEnergy(row, col);
            }
        }
    }
}

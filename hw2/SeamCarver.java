import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private static int BORDER_ENERGY = 1000;
    private Picture picture;

    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    public Picture picture() {
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
        if (x == 0 || x == picture.width() - 1 || y == 0 || y == picture.height() == -1) {
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

    }

    public int[] findVerticalSeam() {

    }

    public void removeHorizontalSeam(int[] seam) {

    }

    public void removeVerticalSeam(int[] seam) {
        
    }
}

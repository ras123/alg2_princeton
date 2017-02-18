public class VerticalSeamStrategy implements SeamStrategy {

    private SeamCarver seamCarver;
    private double[][] energyMatrix;

    public VerticalSeamStrategy(SeamCarver seamCarver, double[][] energyMatrix) {
        this.seamCarver = seamCarver;
        this.energyMatrix = energyMatrix;
    }

    public int getHeight() {
        return seamCarver.height();
    }

    public int getWidth() {
        return seamCarver.width();
    }

    public double getEnergy(int row, int col) {
        return energyMatrix[row][col];
    }
}
public class HorizontalSeamStrategy implements SeamStrategy {

    private SeamCarver seamCarver;
    private double[][] energyMatrix;

    public HorizontalSeamStrategy(SeamCarver seamCarver, double[][] energyMatrix) {
        this.seamCarver = seamCarver;
        this.energyMatrix = energyMatrix;
    }

	public int getHeight() {
        return seamCarver.width();
    }

    public int getWidth() {
        return seamCarver.height();
    }

    public double getEnergy(int row, int col) {
        return energyMatrix[col][row];
    }
}
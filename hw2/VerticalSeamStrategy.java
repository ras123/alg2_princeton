public class VerticalSeamStrategy implements SeamStrategy {

	private double[][] energyMatrix;

	public VerticalSeamStrategy(double[][] energyMatrix) {
		this.energyMatrix = energyMatrix;
	}

	public int getHeight() {
		return energyMatrix.length;
	}

    public int getWidth() {
    	return energyMatrix[0].length;
    }

    public double getEnergy(int row, int col) {
    	return energyMatrix[row][col];
    }
}
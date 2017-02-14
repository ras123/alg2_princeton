public class HorizontalSeamStrategy implements SeamStrategy {

	private double[][] energyMatrix;

	public HorizontalSeamStrategy(double[][] energyMatrix) {
		this.energyMatrix = energyMatrix;
	}

	public int getHeight() {
		return energyMatrix[0].length;
	}

    public int getWidth() {
    	return energyMatrix.length;
    }

    public double getEnergy(int row, int col) {
    	return energyMatrix[col][row];
    }
}
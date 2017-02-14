public interface SeamStrategy {
    public int getHeight();
    public int getWidth();
    public double getEnergy(int row, int col);
}
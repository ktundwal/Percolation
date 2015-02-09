/**
 * Auto Generated Java Class.
 */
public class PercolationStats {

    private final int numberOfGrids;
    private final int gridSize;
    private Percolation pr;
    private double[] threshholds;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {

        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        gridSize = N;
        numberOfGrids = T;

        threshholds = new double[numberOfGrids];

        for (int experimentNumber = 0; experimentNumber < numberOfGrids - 1;
             experimentNumber++) {
            int  openedSites = 0;
            Percolation p = new Percolation(gridSize);
            while (!p.percolates()) {
                int i = StdRandom.uniform(1, gridSize + 1);
                int j = StdRandom.uniform(1, gridSize + 1);
                if (!p.isOpen(i, j)) {
                    p.open(i, j);
                    openedSites++;
                }
            }
            threshholds[experimentNumber]
                    = (double) openedSites / (gridSize * gridSize);
        }
    }

    // samplinte mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshholds);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / (Math.sqrt(numberOfGrids)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / (Math.sqrt(numberOfGrids)));
    }

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException();
        }

        int size = Integer.parseInt(args[0]);
        int numRuns = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(size, numRuns);

        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = "
                + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}

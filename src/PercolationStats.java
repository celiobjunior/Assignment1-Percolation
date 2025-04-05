/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats n T
 *  Dependencies: Percolation.java
 *
 *  This program performs a series of computational experiments (Monte Carlo
 *  simulations) to estimate the percolation threshold.
 *
 *  Given a grid size 'n' and a number of trials 'T' as command-line
 *  arguments, it performs 'T' independent experiments on an n-by-n grid.
 *  In each experiment:
 *    - It creates a Percolation object representing the n-by-n grid.
 *    - It repeatedly opens random sites (chosen uniformly) in the grid
 *      until the system percolates.
 *    - It records the fraction of open sites required for percolation in
 *      that specific trial (number of open sites / (n*n)).
 *
 *  After completing all 'T' trials, it calculates and prints the sample mean,
 *  sample standard deviation, and the 95% confidence interval for the
 *  percolation threshold based on the results of the trials.
 *
 *  Provides methods to:
 *    - Calculate the sample mean of the percolation thresholds.
 *    - Calculate the sample standard deviation of the percolation thresholds.
 *    - Calculate the low endpoint of the 95% confidence interval.
 *    - Calculate the high endpoint of the 95% confidence interval.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{
    private static final double CONFIDENCE_95 = 1.96;
    private final int           t;
    private double[]           threshold;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("constructor arguments must be greater than 0");
        this.t = trials;
        threshold = new double[trials];

        for (int i = 0; i < t; i++)
        {
            Percolation p = new Percolation(n);
            while (!p.percolates())
            {
                int row = StdRandom.uniformInt(n) + 1;
                int col = StdRandom.uniformInt(n) + 1;
                p.open(row, col);
            }
            threshold[i] = (double) p.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return StdStats.stddev(threshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(t));
    }

    // test client (see below)
    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        StdOut.println("mean = " + ps.mean());
        StdOut.println("stddev = " + ps.stddev());
        StdOut.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
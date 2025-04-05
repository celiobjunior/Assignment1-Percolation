/******************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:    (Used by other classes like PercolationVisualizer or PercolationStats)
 *  Dependencies: none
 *
 *  Models an n-by-n percolation system using the Weighted Quick Union-Find
 *  algorithm. Each site in the grid can be either blocked or open. The system
 *  percolates if there is a path of open sites connecting the top row to the
 *  bottom row.
 *
 *  This implementation utilizes two virtual sites:
 *    - A virtual top site (index 0) connected to all sites in the first row (row 1).
 *    - A virtual bottom site (index size - 1) connected to all sites in the
 *      last row (row n).
 *  The system percolates if the virtual top site is connected to the virtual
 *  bottom site in the Union-Find structure.
 *
 *  WARNING: This implementation suffers from the "backwash" problem.
 *  The `isFull()` method checks if a site is connected to the virtual top site.
 *  However, because the virtual bottom site is also part of the same Union-Find
 *  structure, if the system percolates, sites connected to the bottom row (but
 *  not necessarily connected to the top row through an open path) might become
 *  connected to the virtual top site *through* the virtual bottom site.
 *  This can cause `isFull()` to return `true` for sites that are not actually
 *  "full" according to the definition (i.e., not reachable from the top row
 *  via a path of open sites). The `percolates()` method itself works correctly.
 *
 *  Provides methods to:
 *    - Create an n-by-n grid (initially all sites blocked).
 *    - Open a site at a given (row, col).
 *    - Check if a site is open.
 *    - Check if a site is full (connected to the top row - **NOTE: subject to backwash**).
 *    - Count the number of open sites.
 *    - Determine if the entire system percolates.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private final int             n;
    private final int             size;
    private       int            openedSites;
    private       boolean[]      grid;
    private WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n <= 0) throw new IllegalArgumentException("n must be greater than 0");
        this.n = n;
        this.size = (n+1) * (n+1) + 1;
        this.openedSites = 0;
        grid = new boolean[size];
        for (int i = 0; i < size; i++) grid[i] = false;
        uf = new WeightedQuickUnionUF(size);
    }

    private int xyTo1D(int row, int col)
    {
        return row * n + col;
    }

    private boolean isOutOfBounds(int row, int col)
    {
        return (row < 1 || row > n || col < 1 || col > n);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        if (isOutOfBounds(row, col)) throw new IllegalArgumentException("out of bounds");

        if (isOpen(row, col)) return;

        grid[xyTo1D(row, col)] = true;

        if (row == 1) uf.union(xyTo1D(row, col), 0);

        if (row == n) uf.union(xyTo1D(row, col), size - 1);

        if (!isOutOfBounds(row, col + 1) && isOpen(row, col + 1))
            uf.union(xyTo1D(row, col), xyTo1D(row, col + 1));

        if (!isOutOfBounds(row, col - 1) && isOpen(row, col - 1))
            uf.union(xyTo1D(row, col), xyTo1D(row, col - 1));

        if (!isOutOfBounds(row + 1, col) && isOpen(row + 1, col))
            uf.union(xyTo1D(row, col), xyTo1D(row + 1, col));

        if (!isOutOfBounds(row - 1, col) && isOpen(row - 1, col))
            uf.union(xyTo1D(row, col), xyTo1D(row - 1, col));

        openedSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (isOutOfBounds(row, col)) throw new IllegalArgumentException("out of bounds");
        return grid[xyTo1D(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if (isOutOfBounds(row, col)) throw new IllegalArgumentException("out of bounds");
        return uf.find(xyTo1D(row, col)) == uf.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return openedSites;
    }

    // does the system percolate?
    public boolean percolates()
    {
        return uf.find(0) == uf.find(size - 1);
    }

    // test client (optional)
    public static void main(String[] args)
    {
        Percolation p = new Percolation(10);
        p.open(1, 1);
        StdOut.println(p.percolates());
    }
}
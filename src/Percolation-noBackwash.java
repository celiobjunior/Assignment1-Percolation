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
 *  This implementation specifically addresses and avoids the "backwash" problem.
 *  Backwash can occur in simpler models where connecting to a virtual bottom
 *  site might incorrectly mark sites as full (connected to the top) if they are
 *  only connected to the bottom row. This version avoids backwash by using
 *  status flags (bitmasks: OPEN, TOP, BOTTOM) associated with the root of each
 *  connected component in the Union-Find structure, rather than using a virtual
 *  bottom site connected to all bottom-row sites.
 *
 *  Provides methods to:
 *    - Create an n-by-n grid (initially all sites blocked).
 *    - Open a site at a given (row, col).
 *    - Check if a site is open.
 *    - Check if a site is full (connected to the top row via open sites).
 *    - Count the number of open sites.
 *    - Determine if the entire system percolates.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private static final int CLOSED     = 0b000;               // 0
    private static final int OPEN       = 0b100;               // 4
    private static final int TOP        = 0b010 | OPEN;        // 6
    private static final int BOTTOM     = 0b001 | OPEN;        // 5
    private static final int PERCOLATES = OPEN | TOP | BOTTOM; // 7

    private final int             n;
    private       int            openedSites;
    private       boolean        systemPercolates;
    private       byte[]         grid;
    private WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n <= 0) throw new IllegalArgumentException("n must be greater than 0");
        this.n = n;
        int size = (n + 1) * (n + 1);
        this.openedSites = 0;
        this.systemPercolates = false;
        grid = new byte[size];
        for (int i = 0; i < size; i++) grid[i] = CLOSED;
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

        byte status = OPEN;

        if (row == 1) status = TOP;
        if (row == n) status = BOTTOM;
        if (n == 1)   status = PERCOLATES;

        int statusRootNeighbor;
        int rootNeighbor;
        int idxCurrent = xyTo1D(row, col);

        int idxRootNeighbor = xyTo1D(row, col + 1);
        if (!isOutOfBounds(row, col + 1) && isOpen(row, col + 1))
        {
            rootNeighbor = uf.find(idxRootNeighbor);
            statusRootNeighbor = grid[rootNeighbor];
            status |= (byte) statusRootNeighbor;
            uf.union(idxCurrent, idxRootNeighbor);
        }

        idxRootNeighbor = xyTo1D(row, col - 1);
        if (!isOutOfBounds(row, col - 1) && isOpen(row, col - 1))
        {
            rootNeighbor = uf.find(idxRootNeighbor);
            statusRootNeighbor = grid[rootNeighbor];
            status |= (byte) statusRootNeighbor;
            uf.union(idxCurrent, idxRootNeighbor);
        }

        idxRootNeighbor = xyTo1D(row + 1, col);
        if (!isOutOfBounds(row + 1, col) && isOpen(row + 1, col))
        {
            rootNeighbor = uf.find(idxRootNeighbor);
            statusRootNeighbor = grid[rootNeighbor];
            status |= (byte) statusRootNeighbor;
            uf.union(idxCurrent, idxRootNeighbor);
        }

        idxRootNeighbor = xyTo1D(row - 1, col);
        if (!isOutOfBounds(row - 1, col) && isOpen(row - 1, col))
        {
            rootNeighbor = uf.find(idxRootNeighbor);
            statusRootNeighbor = grid[rootNeighbor];
            status |= (byte) statusRootNeighbor;
            uf.union(idxCurrent, idxRootNeighbor);
        }

        int newRoot = uf.find(idxCurrent);
        grid[newRoot] = status;
        grid[idxCurrent] = status;

        if (!systemPercolates)
            if ((grid[newRoot] & PERCOLATES) == PERCOLATES)
                systemPercolates = true;

        openedSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (isOutOfBounds(row, col)) throw new IllegalArgumentException("out of bounds");
        return (grid[xyTo1D(row, col)] & OPEN) == OPEN;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if (isOutOfBounds(row, col)) throw new IllegalArgumentException("out of bounds");
        int root = uf.find(xyTo1D(row, col));
        return (grid[root] & TOP) == TOP;
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return openedSites;
    }

    // does the system percolate?
    public boolean percolates()
    {
        return systemPercolates;
    }

    // test client (optional)
    public static void main(String[] args)
    {
        Percolation p = new Percolation(10);
        p.open(1, 1);
        StdOut.println(p.percolates());
    }
}
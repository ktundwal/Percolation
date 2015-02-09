

public class Percolation {
    
    private WeightedQuickUnionUF uf;
    private boolean[] grid;
    private int size;
    private final int ufSize;
    private final int virtualTopIndex;
    private final int virtualBottomIndex;
    private final int topRow;
    private final int bottomRow;
    private final int leftColumn;
    private final int rightColumn;
    
    // create N-by-N grid, with all sites blocked
    public Percolation(int N) { 
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        
        size = N;
        ufSize = (size * size) + 2;
        
        virtualTopIndex = ufSize-2;
        virtualBottomIndex = ufSize-1;
        
        grid = new boolean[ufSize];
        
        topRow = 0;
        bottomRow = size-1;
        leftColumn = 0;
        rightColumn = size-1;
         
        for (int i = 0; i < size*size; i++) {
            grid[i] = false;
        }
        // Open top and bottom
        grid[virtualTopIndex] = true;
        grid[virtualBottomIndex] = true;
        
        uf = new WeightedQuickUnionUF(ufSize);
    }
    
    // open site (row i, column j) if it is not open already
    public void open(int p, int q) {
        
        int i = p-1;
        int j = q-1;
        
        // validate args
        validateIndices(i, j);
        
        // mark site as open
        int ufIndex = xyTo1D(i, j);
        grid[ufIndex] = true;
        
        // link this site using WeightedQuickUnionUF
        if (i == topRow) {
            connectIfOpen(ufIndex, virtualTopIndex);
            int ufIndexOfBelowRow = xyTo1D(i + 1, j);
            connectIfOpen(ufIndex, ufIndexOfBelowRow);
        }
        else if (i == bottomRow) {
            connectIfOpen(ufIndex, virtualBottomIndex);
            int ufIndexOfAboveRow = xyTo1D(i - 1, j);
            connectIfOpen(ufIndex, ufIndexOfAboveRow);
        }
        else {
            int ufIndexOfAboveRow = xyTo1D(i - 1, j);
            int ufIndexOfBelowRow = xyTo1D(i + 1, j);
            connectIfOpen(ufIndex, ufIndexOfAboveRow);
            connectIfOpen(ufIndex, ufIndexOfBelowRow);
        }
        
        if (j == leftColumn) {
            int ufIndexOfRightColumn = xyTo1D(i, j+1);
            connectIfOpen(ufIndex, ufIndexOfRightColumn);
        }
        else if (j == rightColumn) {
            int ufIndexOfLeftColumn = xyTo1D(i, j-1);
            connectIfOpen(ufIndex, ufIndexOfLeftColumn);
        }
        else {
            int ufIndexOfRightColumn = xyTo1D(i, j+1);
            int ufIndexOfLeftColumn = xyTo1D(i, j-1);
            connectIfOpen(ufIndex, ufIndexOfRightColumn);
            connectIfOpen(ufIndex, ufIndexOfLeftColumn);
        }

        //StdOut.println("p.percolates(); => " + percolates());
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int p, int q) {
        int i = p - 1;
        int j = q - 1;
        validateIndices(i, j);
        return grid[xyTo1D(i, j)];
    }
    
    // is site (row i, column j) full?
    public boolean isFull(int p, int q) {
        int i = p - 1;
        int j = q - 1;
        int ufIndex = xyTo1D(i, j);
        boolean full = uf.connected(ufIndex, virtualTopIndex);
        if (full) {
            //StdOut.println(ufIndex + " site is full: " + full);
        }
        return full;
    }
    
    // map 2d to 1d coords
    private int xyTo1D(int i, int j) {
        return i * size + j;
    }
    
    private void validateIndices(int i, int j) {
        if (i > size-1 || j > size-1) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    private void connectIfOpen(int ufIndex, int targetUfIndex) {
        //StdOut.println("Going to connect " + ufIndex + " to " + targetUfIndex);
        //StdOut.println("...." + targetUfIndex + " site is open = "
        //                   + grid[targetUfIndex]);
        //StdOut.println("...." + ufIndex + " site is open = " + grid[ufIndex]);
        if (grid[targetUfIndex]) {
            uf.union(ufIndex, targetUfIndex);
            //StdOut.println("..........find(" + ufIndex + ") = "
            //                   + uf.find(ufIndex));
            //StdOut.println("..........find(" + targetUfIndex + ") = "
            //                   + uf.find(targetUfIndex));
        }
    }
    
    // does the system percolate?
    public boolean percolates() {
        //StdOut.println("find(virtualTopIndex) = " + uf.find(virtualTopIndex));
        //StdOut.println("find(virtualBottomIndex) = "
        // + uf.find(virtualBottomIndex));
        return (uf.connected(virtualTopIndex, virtualBottomIndex));
    }
    
    // test client
    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        openSiteAndLog(p, 1, 1);
        openSiteAndLog(p, 2, 1);
        openSiteAndLog(p, 3, 1);
        openSiteAndLog(p, 4, 1);
        openSiteAndLog(p, 5, 1);
        
        //StdOut.println("p.percolates(); => " + p.percolates());
        
    }
    
    private static void openSiteAndLog(Percolation p, int i, int j) {
        p.open(i, j);
        //StdOut.println("p.isOpen(" + i + "," + j + "); => " + p.isOpen(i, j));
    }
}

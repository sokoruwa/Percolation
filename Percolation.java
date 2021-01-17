import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int size;
    private WeightedQuickUnionUF backwashBox;
    private WeightedQuickUnionUF box;
    private int virtualTop;
    private int virtualBottom;
    private int numOpenSites;

    // converts the coordinates of the grid from a two-dimensional value to a one-dimensional value
    private int cordConverter(int row, int col) {
        int x = row;
        int y = col;
        return ((y * size) + x);
    }

    // checks if coordinates are in range of the grid
    private boolean inRange(int row, int col) {
        if ((row < 0 || col < 0 || row > size - 1 || col > size - 1)) {
            return false;
            // throw new IllegalArgumentException("out of range"); // retu

        } else {
            return true;
        }
    }


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.size = n;
        virtualTop = size * size;
        virtualBottom = (size * size) + 1;

        if (size <= 0) {
            throw new IllegalArgumentException("invalid grid");
        } else {
            grid = new boolean[n][n];

            box = new WeightedQuickUnionUF(size * size + 2);
            backwashBox = new WeightedQuickUnionUF(size * size + 1);
        }


    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        int value = cordConverter(row, col);
//       virtualTop = 0;
        if (row == 0) {
            box.union(cordConverter(row, col), virtualTop);
            backwashBox.union(cordConverter(row, col), virtualTop);
        }


        if (!inRange(row, col)) {
            throw new IllegalArgumentException("out of range");
        }

        if (isOpen(row, col)) { // if it's already open
            return;
        }
        if (inRange(row - 1, col) && isOpen(row - 1, col)) { // the value on top
            box.union(value, cordConverter(row - 1, col));
            backwashBox.union(value, cordConverter(row - 1, col));
        }
        if (inRange(row + 1, col) && isOpen(row + 1, col)) { // the value at the bottom
            box.union(value, cordConverter(row + 1, col));
            backwashBox.union(value, cordConverter(row + 1, col));
        }
        if (inRange(row, col - 1) && isOpen(row, col - 1)) { // the value on the left
            box.union(value, cordConverter(row, col - 1));
            backwashBox.union(value, cordConverter(row, col - 1));
        }
        if (inRange(row, col + 1) && isOpen(row, col + 1)) { // the value on the right
            box.union(value, cordConverter(row, col + 1));
            backwashBox.union(value, cordConverter(row, col + 1));
        }


        grid[row][col] = true;


        numOpenSites++;


        if (row == size - 1 && isOpen(row, col)) { // if any site at the bottom is open, connect it to the imaginary bottom
//            virtualBottom = (size * size) + 1;
            box.union(cordConverter(row, col), virtualBottom);
        }

    }


    // is the site (row, col) open?

    public boolean isOpen(int row, int col) {
        if (!inRange(row, col)) {
            throw new IllegalArgumentException("out of range");
        } else {
            //inRange(row, col);
            if (grid[row][col]) {
                return true;
            }
            return false;


            //return isOpen(row, col) && box.connected(cordConverter(row, col), cordConverter(0, size));
        }
    }

//

    // is the site (row, col) full?
    // for it to be full the top has to be connected to the site
    public boolean isFull(int row, int col) {
        int value = cordConverter(row, col);
        if (!inRange(row, col)) {
            throw new IllegalArgumentException("out of range");
        }

        // checks if site is open and connected to the virtual top
        return backwashBox.connected(value, virtualTop); //isOpen(row, col) &&
        //}
//        return false;

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
//        int virtualTop = cordConverter(0, size);
//        int virtualBottom = cordConverter(1, size); // int bottom = cordConverter(size-1, size); ??

        return box.connected(virtualTop, virtualBottom);
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation object = new Percolation(5);
        object.open(0, 0);
        object.open(1, 0);
        object.open(2, 0);
        object.isOpen(2, 0);
        object.isFull(2, 0);
        object.numberOfOpenSites();
        object.percolates();
        System.out.println(object.isOpen(2, 0));
        System.out.println(object.isFull(2, 0));
    }
}

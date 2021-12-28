package com.uet.oop.AiUnits;

import java.util.Stack;

public class Cell {
    public static final int ROW = 18;
    public static final int COL = 18;
    private int parent_i;
    private int parent_j;
    private double f, g, h;

    public Cell() {
        parent_i = 0;
        parent_j = 0;
        f = 0; g = 0; h = 0;
    }

    public Cell(double f, double g, double h, int parent_i, int parent_j) {
        this.g = g; this.h = h; this.f = f; this.parent_j = parent_j; this.parent_i = parent_i;
    }

    public double getF() {
        return f;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

    public int getParent_i() {
        return parent_i;
    }

    public int getParent_j() {
        return parent_j;
    }

    public void setF(double f) {
        this.f = f;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setParent_i(int parent_i) {
        this.parent_i = parent_i;
    }

    public void setParent_j(int parent_j) {
        this.parent_j = parent_j;
    }

    public static boolean isValid(int row, int col) {
        // Returns true if row number and column number
        // is in range
        return (row >= 0) && (row < ROW) && (col >= 0)
                && (col < COL);
    }

    // A Utility Function to check whether the given cell is
    // blocked or not
    public static boolean isUnBlocked(int grid[][], int row, int col) {
        // Returns true if the cell is not blocked else false
        if (grid[row][col] == 1)
            return true;
        else
            return false;
    }

    // A Utility Function to check whether destination cell has
    // been reached or not
    public static boolean isDestination(int row, int col, PairCoordinate dest) {
        if (row == dest.getY() && col == dest.getX())
            return true;
        else
            return false;
    }

    // A Utility Function to calculate the 'h' heuristics.
    public static double calculateHValue(int row, int col, PairCoordinate dest)
    {
        // Return using the Manhattan Distance Formula.
        return (Math.sqrt(Math.pow(row - dest.getY(), 2)
                + Math.pow(col - dest.getX(), 2)));
    }

    // A Utility Function to trace the path from the source
    // to destination
    public static void tracePath(Stack<PairCoordinate> Path, Cell[][] cellDetails, PairCoordinate dest)
    {
        // System.out.print("\nThe Path is ");
        int row = dest.getY();
        int col = dest.getX();

        while (Cell.isValid(col, row) && !(cellDetails[row][col].getParent_i() == row
                && cellDetails[row][col].getParent_j() == col)) {
            Path.push(new PairCoordinate(col, row));
            int temp_row = cellDetails[row][col].getParent_i();
            int temp_col = cellDetails[row][col].getParent_j();
            row = temp_row;
            col = temp_col;
        }
        if (Cell.isValid(col, row))     Path.push(new PairCoordinate(col, row));

//        while (!Path.empty()) {
//            PairCoordinate p = Path.peek();
//            Path.pop();
//            System.out.printf("-> (%d,%d) ", p.getY(), p.getX());
//        }
//        return;
    }
}


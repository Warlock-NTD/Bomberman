package com.uet.oop.AiUnits;

import java.util.*;

public class AI {

    public AI() {}

    public static int agentAction(String map, int xAgent, int yAgent) {
        return 0;
    }

    public static Queue<Integer> botAction(String map, int xBot, int yBot, int xAgent, int yAgent) {
        /* Description of the Grid-
         * 1--> The cell is un blocked or Policy self defined.
         * 0--> The cell is blocked or Policy self defined.
         */
        int [][] grid = readData(map);

        // Source is the left-most bottom-most corner
        PairCoordinate src = new PairCoordinate(xBot, yBot);

        // Destination is the left-most top-most corner
        PairCoordinate dest = new PairCoordinate(xAgent, yAgent);

        Stack<PairCoordinate> sequence = heuristicSearch(grid, src, dest);
        for (PairCoordinate p : sequence) {
            System.out.print("[ " + p.getX() + ", " + p.getY() + "] - ");
        }
        System.out.println();

        Queue<Integer> res = new LinkedList<>();
        int xtemp = xBot, ytemp = yBot;
        while (!sequence.isEmpty()) {
            PairCoordinate pk = sequence.pop();
            if (pk.getY() == ytemp) {
                if (pk.getX() > xtemp) {
                    res.add(1);
                    xtemp = pk.getX();
                } else if (pk.getX() < xtemp) {
                    res.add(0);
                    xtemp = pk.getX();
                }
            }
            if (pk.getX() == xtemp) {
                if (pk.getY() > ytemp) {
                    res.add(3);
                    ytemp = pk.getY();
                } else if (pk.getY() < ytemp) {
                    res.add(2);
                    ytemp = pk.getY();
                }
            }
        }

        if (res.isEmpty()) {
            Random rn = new Random();
            res.add(rn.nextInt() % 4);
            res.add(rn.nextInt() % 4);
            res.add(rn.nextInt() % 4);
            res.add(rn.nextInt() % 4);
        }
        System.out.println(res);

        return res;
    }


    public static int[][] readData(String map) {
        int[][] grid = new int[18][18];
        int row = 0;
        Scanner myReader = new Scanner(map);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            // System.out.println(data);
            for (int col = 0; col < data.length(); ++col) {
                if (data.charAt(col) == '#' || data.charAt(col) == '=') {
                    grid[row][col] = 0;
                    // This part for handling Solid Things.
                } else if (data.charAt(col) == '*') {
                    // This part for handling bomb impaction.
		    if (row > 1)    grid[row - 2][col] = 0;
                    if (row > 0)    grid[row - 1][col] = 0;
                    if (row < 17)   grid[row + 1][col] = 0;
		    if (row < 16)  grid[row + 2][col] = 0;
                    if (col > 0)   grid[row][col - 1] = 0;
		    if (col > 1)   grid[row][col - 2] = 0;
                    if (col < 17)   grid[row][col + 1] = 0;
		    if (col < 16) grid[row][col + 2] = 0;
                    grid[row][col] = 0;
                } else {
                    // This part corresponding the empty cell.
                    grid[row][col] = 1;
                }
            }
            row++;
        }
        myReader.close();
        return grid;
    }

    public static Stack<PairCoordinate> heuristicSearch(int[][] grid, PairCoordinate src, PairCoordinate dest) {
        Stack<PairCoordinate> Path = new Stack<>();
        System.out.printf("Bommer : %d - %d%nBot : %d - %d%n", dest.getX(), dest.getY(), src.getX(), src.getY());
        // If the source is out of range
        if (!Cell.isValid(src.getY(), src.getX())) {
            System.out.print("Source is invalid\n");
            return Path;
        }

        // If the destination is out of range
        if (!Cell.isValid(dest.getY(), dest.getX())) {
            System.out.print("Destination is invalid\n");
            return Path;
        }

        // Either the source or the destination is blocked
        if (!Cell.isUnBlocked(grid, src.getY(), src.getX())
                || !Cell.isUnBlocked(grid, dest.getY(), dest.getX())) {
            System.out.print("Source or the destination is blocked\n");
            return Path;
        }

        // If the destination cell is the same as source cell
        if (Cell.isDestination(src.getY(), src.getX(), dest)) {
            System.out.print("We are already at the destination\n");
            return Path;
        }

        // Create a closed list and initialise it to false which
        // means that no cell has been included yet This closed
        // list is implemented as a boolean 2D array
        boolean [][] closedList = new boolean[Cell.ROW][Cell.COL];

        // Declare a 2D array of structure to hold the details
        // of that cell
        Cell [][] cellDetails = new Cell[Cell.ROW][Cell.COL];


        int i, j;

        for (i = 0; i < Cell.ROW; i++) {
            for (j = 0; j < Cell.COL; j++) {
                cellDetails[i][j] = new Cell(Double.MAX_VALUE, Double.MAX_VALUE
                        , Double.MAX_VALUE, -1, -1);
                closedList[i][j] = false;
            }
        }

        // Initialising the parameters of the starting node
        i = src.getY(); j = src.getX();
        cellDetails[i][j].setF(0);
        cellDetails[i][j].setG(0);
        cellDetails[i][j].setH(0);
        cellDetails[i][j].setParent_i(i);
        cellDetails[i][j].setParent_j(j);

	/*
	Create an open list having information as-
	<f, <i, j>>
	where f = g + h,
	and i, j are the row and column index of that cell
	Note that 0 <= i <= ROW-1 & 0 <= j <= COL-1
	This open list is implemented as a set pair of pair.
	*/

        Set<IntegerToPair> openList = new HashSet<>();

        // Put the starting cell on the open list and set its
        // 'f' as 0
        openList.add(new IntegerToPair(0.0, new PairCoordinate(j, i)));

        // We set this boolean value as false as initially
        // the destination is not reached.
        boolean foundDest = false;

        // Set<IntegerToPair> openListClone = new HashSet<>(openList);

        while (!openList.isEmpty()) {
            // Add this vertex to the closed list
            IntegerToPair ip = openList.iterator().next();
            i = ip.getP().getY();
            j = ip.getP().getX();
            closedList[i][j] = true;
            openList.remove(ip);

            // Travel Up...
            if (Cell.isValid(i - 1, j)) {
                // If the destination cell is the same as the
                // current successor
                if (Cell.isDestination(i - 1, j, dest)) {
                    // Set the Parent of the destination cell
                    cellDetails[i - 1][j].setParent_i(i);
                    cellDetails[i - 1][j].setParent_j(j);;
                    System.out.print("The destination cell is found\n");
                    Cell.tracePath(Path, cellDetails, dest);
                    foundDest = true;
                    return Path;
                } else if (!closedList[i - 1][j] && Cell.isUnBlocked(grid, i - 1, j)) {
                    double gNew = cellDetails[i][j].getG() + 1.0;
                    double hNew = Cell.calculateHValue(i - 1, j, dest);
                    double fNew = gNew + hNew;

                    // If it isn’t on the open list, add it to
                    // the open list. Make the current square
                    // the parent of this square. Record the
                    // f, g, and h costs of the square cell
                    //			 OR
                    // If it is on the open list already, check
                    // to see if this path to that square is
                    // better, using 'f' cost as the measure.
                    if (Double.valueOf(cellDetails[i - 1][j].getF()).equals(Double.MAX_VALUE)
                            || cellDetails[i - 1][j].getF() > fNew) {
                        openList.add(new IntegerToPair(fNew, new PairCoordinate(j, i - 1)));
                        // Update the details of this cell
                        cellDetails[i - 1][j].setF(fNew);
                        cellDetails[i - 1][j].setG(gNew);
                        cellDetails[i - 1][j].setH(hNew);
                        cellDetails[i - 1][j].setParent_i(i);
                        cellDetails[i - 1][j].setParent_j(j);
                    }
                }
            }

            // Travel Down...
            // Only process this cell if this is a valid one
            if (Cell.isValid(i + 1, j)) {
                // If the destination cell is the same as the
                // current successor
                if (Cell.isDestination(i + 1, j, dest)) {
                    // Set the Parent of the destination cell
                    cellDetails[i + 1][j].setParent_i(i);
                    cellDetails[i + 1][j].setParent_j(j);
                    System.out.print("The destination cell is found\n");
                    Cell.tracePath(Path, cellDetails, dest);
                    foundDest = true;
                    return Path;
                }
                // If the successor is already on the closed
                // list or if it is blocked, then ignore it.
                // Else do the following
                else if (!closedList[i + 1][j] && Cell.isUnBlocked(grid, i + 1, j)) {
                    double gNew = cellDetails[i][j].getG() + 1.0;
                    double hNew = Cell.calculateHValue(i + 1, j, dest);
                    double fNew = gNew + hNew;

                    // If it isn’t on the open list, add it to
                    // the open list. Make the current square
                    // the parent of this square. Record the
                    // f, g, and h costs of the square cell
                    //			 OR
                    // If it is on the open list already, check
                    // to see if this path to that square is
                    // better, using 'f' cost as the measure.
                    if (cellDetails[i + 1][j].getF() == Double.MAX_VALUE
                            || cellDetails[i + 1][j].getF() > fNew) {
                        openList.add(new IntegerToPair(fNew, new PairCoordinate(j, i + 1)));
                        // Update the details of this cell
                        cellDetails[i + 1][j].setF(fNew);
                        cellDetails[i + 1][j].setG(gNew);
                        cellDetails[i + 1][j].setH(hNew);
                        cellDetails[i + 1][j].setParent_i(i);
                        cellDetails[i + 1][j].setParent_j(j);
                    }
                }
            }

            // Travel Right...
            // Only process this cell if this is a valid one
            if (Cell.isValid(i, j + 1)) {
                // If the destination cell is the same as the
                // current successor
                if (Cell.isDestination(i, j + 1, dest)) {
                    // Set the Parent of the destination cell
                    cellDetails[i][j + 1].setParent_i(i);
                    cellDetails[i][j + 1].setParent_j(j);
                    System.out.print("The destination cell is found\n");
                    Cell.tracePath(Path, cellDetails, dest);
                    foundDest = true;
                    return Path;
                }
                // If the successor is already on the closed
                // list or if it is blocked, then ignore it.
                // Else do the following
                else if (!closedList[i][j + 1] && Cell.isUnBlocked(grid, i, j + 1)) {
                    double gNew = cellDetails[i][j].getG() + 1.0;
                    double hNew = Cell.calculateHValue(i, j + 1, dest);
                    double fNew = gNew + hNew;

                    // If it isn’t on the open list, add it to
                    // the open list. Make the current square
                    // the parent of this square. Record the
                    // f, g, and h costs of the square cell
                    //			 OR
                    // If it is on the open list already, check
                    // to see if this path to that square is
                    // better, using 'f' cost as the measure.
                    if (cellDetails[i][j + 1].getF() == Double.MAX_VALUE
                            || cellDetails[i][j + 1].getF() > fNew) {
                        openList.add(new IntegerToPair(fNew, new PairCoordinate(j + 1, i)));
                        // Update the details of this cell
                        cellDetails[i][j + 1].setF(fNew);
                        cellDetails[i][j + 1].setG(gNew);
                        cellDetails[i][j + 1].setH(hNew);
                        cellDetails[i][j + 1].setParent_i(i);
                        cellDetails[i][j + 1].setParent_j(j);
                    }
                }
            }

            // Travel Left...
            // Only process this cell if this is a valid one
            if (Cell.isValid(i, j - 1)) {
                // If the destination cell is the same as the
                // current successor
                if (Cell.isDestination(i, j - 1, dest)) {
                    // Set the Parent of the destination cell
                    cellDetails[i][j - 1].setParent_i(i);
                    cellDetails[i][j - 1].setParent_j(j);
                    System.out.print("The destination cell is found\n");
                    Cell.tracePath(Path, cellDetails, dest);
                    foundDest = true;
                    return Path;
                }
                // If the successor is already on the closed
                // list or if it is blocked, then ignore it.
                // Else do the following
                else if (!closedList[i][j - 1] && Cell.isUnBlocked(grid, i, j - 1)) {
                    double gNew = cellDetails[i][j].getG() + 1.0;
                    double hNew = Cell.calculateHValue(i, j - 1, dest);
                    double fNew = gNew + hNew;

                    // If it isn’t on the open list, add it to
                    // the open list. Make the current square
                    // the parent of this square. Record the
                    // f, g, and h costs of the square cell
                    //			 OR
                    // If it is on the open list already, check
                    // to see if this path to that square is
                    // better, using 'f' cost as the measure.
                    if (cellDetails[i][j - 1].getF() == Double.MAX_VALUE
                            || cellDetails[i][j - 1].getF() > fNew) {
                        openList.add(new IntegerToPair(fNew, new PairCoordinate(j - 1, i)));
                        // Update the details of this cell
                        cellDetails[i][j - 1].setF(fNew);
                        cellDetails[i][j - 1].setG(gNew);
                        cellDetails[i][j - 1].setH(hNew);
                        cellDetails[i][j - 1].setParent_i(i);
                        cellDetails[i][j - 1].setParent_j(j);
                    }
                }
            }

        }
        // When the destination cell is not found and the open
        // list is empty, then we conclude that we failed to
        // reach the destination cell. This may happen when the
        // there is no way to destination cell (due to
        // blockages)
        if (!foundDest) {
            System.out.print("Failed to find the Destination Cell\n");
        }
        return Path;
    }
}
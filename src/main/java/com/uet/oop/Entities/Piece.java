package com.uet.oop.Entities;

public abstract class Piece {
    public static int AUTO_INCREMENT_INDEX = 0;
    private int coordinatesX;
    private int coordinatesY;
    private int index;

    public Piece() {
        index = ++AUTO_INCREMENT_INDEX;
    }

    public Piece(int coordinatesX, int coordinatesY) {
        this.coordinatesX = coordinatesX;
        this.coordinatesY = coordinatesY;
        index = ++AUTO_INCREMENT_INDEX;
    }

    public int getIndex() {
        return index;
    }

    public int getCoordinatesX() {
        return coordinatesX;
    }

    public void setCoordinatesX(int coordinatesX) {
        this.coordinatesX = coordinatesX;
    }

    public int getCoordinatesY() {
        return coordinatesY;
    }

    public void setCoordinatesY(int coordinatesY) {
        this.coordinatesY = coordinatesY;
    }

    /**
     * This function below is called to check this position can be placed by piece
     * * if (x, y) is a static entity, it cannot be placed
     * @param direction in range[0, 3]
     */
    public abstract boolean canMove(Board board, int direction);

    /**
     * This function is called to get the symbol of piece
     */
    public abstract String getSymbol();

    /**
     * This function is called to move piece to one in four directions : (0) left, (1) right, (2) up, (3) down
     * @param direction in range[0, 3]
     */
    public abstract void move(int direction);

    public boolean checkPosition(int x, int y) {
        return coordinatesX == x && coordinatesY == y;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Piece other)) return false;
        return other.getIndex() == index;
    }
}

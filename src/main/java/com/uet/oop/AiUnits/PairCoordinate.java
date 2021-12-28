package com.uet.oop.AiUnits;

public class PairCoordinate {
    private int x;
    private int y;

    public PairCoordinate(int x, int y) {
        this.y = y;
        this.x = x;
    }

    public int hashCode() {
        return 23 * (23 + x) + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PairCoordinate))
            return false;
        PairCoordinate p = (PairCoordinate) obj;
        return (p.getX() == x) && (p.getY() == y);
    }
}


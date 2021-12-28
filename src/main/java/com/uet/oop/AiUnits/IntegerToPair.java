package com.uet.oop.AiUnits;

public class IntegerToPair {
    private double f;
    private PairCoordinate p;

    public IntegerToPair(double f, PairCoordinate ip) {
        this.f = f;
        this.p = ip;
    }

    public int hashCode() {
        return 23 * (23 + Double.valueOf(f).hashCode()) + p.hashCode();
    }

    public double getF() {
        return f;
    }

    public PairCoordinate getP() {
        return p;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof IntegerToPair)) {
            return false;
        }
        IntegerToPair ip = (IntegerToPair) obj;
        return Double.valueOf(f).equals(ip.getF())
                && ip.getP().equals(p);
    }
}


package com.uet.oop.Entities;

import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Bomberman extends Piece {
    public static final double DURATION = 0.25;//seconds
    public static final double STUNNED_TIME = 1.5;
    public static final double COEFFICENT = 1.1;
    private List<Image> standingImages;
    private List<Image> movingImages;
    private String color;
    private int numOfBombs = 1;
    private int healthPoint = 3;
    private long lastTimeMove;
    private Queue<Long> lastTimeBomb = new LinkedList<>();
    private int lastMoveDirection = 3;
    private long stunStartTime;

    public Bomberman(int coordinatesX, int coordinatesY) {
        super(coordinatesX, coordinatesY);
        setColor("Yellow");
    }
    public Bomberman(int coordinatesX, int coordinatesY, String color) {
        super(coordinatesX, coordinatesY);
        setColor(color);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        loadImages();
    }

    public boolean ableToMove() {
        return (System.nanoTime() - lastTimeMove >= DURATION * COEFFICENT * 1e9) && !isStunned();
    }

    public boolean isAlive() {
        return healthPoint > 0;
    }

    public int getNumOfBombs() {
        return numOfBombs;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void useBomb() {
        numOfBombs--;
        lastTimeBomb.add(System.nanoTime());
    }

    public void equipBomb() {
        numOfBombs++;
    }

    public void bleed() {
        healthPoint--;
        if (healthPoint <= 0) System.out.println("dead");
        else stunStartTime = System.currentTimeMillis();
    }

    public boolean isStunned() {
        return (System.currentTimeMillis() - stunStartTime < STUNNED_TIME * 1e3);
    }

    public void heal() {
        healthPoint++;
    }

    public void restoreBomb() {
        if (lastTimeBomb.isEmpty()) return;
        while (!lastTimeBomb.isEmpty()) {
            if (System.nanoTime() - lastTimeBomb.peek() < Bomb.EXISTED_TIME * 1e9) break;
            equipBomb();
            lastTimeBomb.clear();
        }
    }

    private void loadImages() {
        String path = "src/main/resources/com/uet/oop/Images/Bomberman/" + color;
        standingImages = new ArrayList<>();
        standingImages.add(new Image(new File(path + "/00.gif").toURI().toString()));
        standingImages.add(new Image(new File(path + "/11.gif").toURI().toString()));
        standingImages.add(new Image(new File(path + "/22.gif").toURI().toString()));
        standingImages.add(new Image(new File(path + "/33.gif").toURI().toString()));
        //
        movingImages = new ArrayList<>();
        movingImages.add(new Image(new File(path + "/0.gif").toURI().toString()));
        movingImages.add(new Image(new File(path + "/1.gif").toURI().toString()));
        movingImages.add(new Image(new File(path + "/2.gif").toURI().toString()));
        movingImages.add(new Image(new File(path + "/3.gif").toURI().toString()));
        //
        movingImages.add(new Image(new File(path + "/4.gif").toURI().toString()));
    }

    public Image getStandingImage() {
        return standingImages.get(lastMoveDirection);
    }

    public Image getMoveImage() {
        return movingImages.get(lastMoveDirection);
    }

    public Image getExplosionImage() {
        return movingImages.get(4);
    }

    public boolean isInExplosionRangeOf(Bomb bomb, Board board) {
        int x1 = bomb.getCoordinatesX();
        int y1 = bomb.getCoordinatesY();
        int x2 = super.getCoordinatesX();
        int y2 = super.getCoordinatesY();
        if (x1 == x2 && Math.abs(y1 - y2) <= Bomb.RADIUS) {
            for (int step = (y1 < y2 ? 1 : -1); y1 != y2; y1 += step) {
                if (board.getAt(x1, y1) instanceof Stone) return false;
            }
            return true;
        }
        if (y1 == y2 && Math.abs(x1 - x2) <= Bomb.RADIUS) {
            for (int step = (x1 < x2 ? 1 : -1); x1 != x2; x1 += step) {
                if (board.getAt(x1, y1) instanceof Stone) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canMove(Board board, int direction) {
        int x = super.getCoordinatesX();
        int y = super.getCoordinatesY();
        switch (direction) {
            case (2) -> y = y - 1;
            case (3) -> y = y + 1;
            case (0) -> x = x - 1;
            case (1) -> x = x + 1;
        }
        Piece piece;
        if ((piece = board.getAt(x, y)) == null) return true;
        return !(piece instanceof Stone) && !(piece instanceof Brick) && !(piece instanceof Bomb);
    }

    /**
     * Symbol of bomberman is "B"
     */
    @Override
    public String getSymbol() {
        return "$";
    }

    @Override
    public void move(int direction) {
        int x = super.getCoordinatesX();
        int y = super.getCoordinatesY();
        switch (direction) {
            case (2) -> {
                super.setCoordinatesY(y - 1);
                lastTimeMove = System.nanoTime();
            }
            case (3) -> {
                super.setCoordinatesY(y + 1);
                lastTimeMove = System.nanoTime();
            }
            case (0) -> {
                super.setCoordinatesX(x - 1);
                lastTimeMove = System.nanoTime();
            }
            case (1) -> {
                super.setCoordinatesX(x + 1);
                lastTimeMove = System.nanoTime();
            }
        }
        lastMoveDirection = direction;
    }
}

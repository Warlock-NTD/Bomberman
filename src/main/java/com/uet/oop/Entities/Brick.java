package com.uet.oop.Entities;

import javafx.scene.image.Image;

import java.io.File;
import java.util.Random;

public class Brick extends Piece {
    public static final double DURATION = 0.5;//seconds
    public static final int BONUS_RANDOM_RANGE = 20;
    private boolean isBroken;
    private Image standingImage;
    private Image explorationImage;
    private Bonus containedBonus; // (-1) not contains; (0) contains bomb; (1) contains health point

    public Brick() {
        loadImages();
        loadBonus();
    }

    public Brick(int coordinatesX, int coordinatesY) {
        super(coordinatesX, coordinatesY);
        this.isBroken = false;
        loadImages();
        loadBonus();
    }

    private void loadImages() {
        standingImage
                = new Image(new File("src//main//resources//com//uet//oop//Images//BrickFires//1.png").toURI().toString());
        explorationImage
                = new Image(new File("src//main//resources//com//uet//oop//Images//BrickFires//1.gif").toURI().toString());
    }

    public Image getStandingImage() {
        return standingImage;
    }

    public Image getExplosionImage() {
        return explorationImage;
    }

    private void loadBonus() {
        Random random = new Random();
        int result = random.nextInt(BONUS_RANDOM_RANGE);
        if (result == 0) containedBonus = new Bonus(super.getCoordinatesX(), super.getCoordinatesY(), 0);
        else if (result == 1) containedBonus = new Bonus(super.getCoordinatesX(), super.getCoordinatesY(), 1);
        else if (result == 2) containedBonus = new Bonus(super.getCoordinatesX(), super.getCoordinatesY(), 2);
        else containedBonus = null;
    }

    public Bonus getContainedBonus() {
        return containedBonus;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void breakBrick() {
        isBroken = true;
    }

    /**
     * Brick is static, it cannot move
     */
    @Override
    public boolean canMove(Board board, int direction) {
        return false;
    }

    @Override
    public String getSymbol() {
        return "=";
    }

    @Override
    public void move(int direction) {

    }
}

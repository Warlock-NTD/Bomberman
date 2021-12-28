package com.uet.oop.Entities;

import javafx.scene.image.Image;

import java.io.File;

public class Stone extends Piece {
    private Image standingImage;

    public Stone() {
        loadImage();
    }

    public Stone(int coordinatesX, int coordinatesY) {
        super(coordinatesX, coordinatesY);
        loadImage();
    }

    public void loadImage() {
        standingImage = new Image(new File("src//main//resources//com//uet//oop//Images//BrickFires//28.gif").toURI().toString());
    }

    public Image getStandingImage() {
        return standingImage;
    }

    /**
     * Stone is static, it cannot move
     */
    @Override
    public boolean canMove(Board board, int direction) {
        return false;
    }

    @Override
    public String getSymbol() {
        return "#";
    }

    @Override
    public void move(int direction) {

    }
}

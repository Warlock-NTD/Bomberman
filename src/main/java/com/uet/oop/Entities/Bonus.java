package com.uet.oop.Entities;

import javafx.scene.image.Image;

import java.io.File;

public class Bonus extends Piece {
    public static final int TIME_BONUS = 15;//seconds
    private boolean isClaimed = false;
    private int type; // (0) bomb, (1) health point, (2) time
    private Image standingImage;

    public Bonus(int coordinatesX, int coordinatesY) {
        super(coordinatesX, coordinatesY);
        setType(0);
    }

    public Bonus(int coordinatesX, int coordinatesY, int type) {
        super(coordinatesX, coordinatesY);
        setType(type);
    }

    public boolean isClaimed() {
        return isClaimed;
    }

    public void setClaimed() {
        isClaimed = true;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = Math.abs(type) % 3;
        loadImage();
    }

    private void loadImage() {
        switch (type) {
            case 0 -> standingImage = new Image(new File("src/main/resources/com/uet/oop/Images/Bonuses/bonus1.gif").toURI().toString());
            case 1 -> standingImage = new Image(new File("src/main/resources/com/uet/oop/Images/Bonuses/bonus2.gif").toURI().toString());
            case 2 -> standingImage = new Image(new File("src/main/resources/com/uet/oop/Images/Bonuses/bonus3.gif").toURI().toString());
        }
    }

    public Image getStandingImage() {
        return standingImage;
    }

    @Override
    public boolean canMove(Board board, int direction) {
        return false;
    }

    @Override
    public String getSymbol() {
        String res = "";
        switch(type) {
            case (0) -> res = "ơ";
            case (1) -> res = "+";
            case (2) -> res = "@";
        }
        return res;
    }

    @Override
    public void move(int direction) {

    }
}

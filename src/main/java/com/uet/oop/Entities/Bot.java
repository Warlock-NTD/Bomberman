package com.uet.oop.Entities;

import javafx.scene.image.Image;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Bot extends Piece {
    public static final double DURATION = 2; //seconds;
    private int level;
    private Image moveImage;
    private Image deadImage;
    private long lastTimeMove;
    private Bonus containedBonus;
    private Queue<Integer> sequenceAction = new LinkedList<>();

    public Bot(int coordinatesX, int coordinatesY) {
        super(coordinatesX, coordinatesY);
        setLevel(1);
        loadBonus();
    }

    public Bot(int coordinatesX, int coordinatesY, int level) {
        super(coordinatesX, coordinatesY);
        setLevel(level);
        loadBonus();
    }

    private void loadImages() {
        String mp = "", dp = "";
        switch (level) {
            case (1) -> {
                mp = "src/main/resources/com/uet/oop/Images/Bot/0.gif";
                dp = "src/main/resources/com/uet/oop/Images/Bot/00.gif";
            }
            case (2) -> {
                mp = "src/main/resources/com/uet/oop/Images/Bot/1.gif";
                dp = "src/main/resources/com/uet/oop/Images/Bot/11.gif";
            }
            case (3) -> {
                mp = "src/main/resources/com/uet/oop/Images/Bot/2.gif";
                dp = "src/main/resources/com/uet/oop/Images/Bot/22.gif";
            }
            case (4) -> {
                mp = "src/main/resources/com/uet/oop/Images/Bot/3.gif";
                dp = "src/main/resources/com/uet/oop/Images/Bot/33.gif";
            }
            case (5) -> {
                mp = "src/main/resources/com/uet/oop/Images/Bot/4.gif";
                dp = "src/main/resources/com/uet/oop/Images/Bot/44.gif";
            }
        }
        moveImage = new Image(new File(mp).toURI().toString());
        if (moveImage.isError()) System.err.println("error");
        deadImage = new Image(new File(dp).toURI().toString());
        if (deadImage.isError()) System.err.println("error");
    }

    public Image getMoveImage() {
        return moveImage;
    }

    public Image getDeadImage() {
        return deadImage;
    }

    private void loadBonus() {
        Random random = new Random();
        int result = random.nextInt(Brick.BONUS_RANDOM_RANGE / 2);
        if (result == 0) containedBonus = new Bonus(super.getCoordinatesX(), super.getCoordinatesY(), 0);
        else if (result == 1) containedBonus = new Bonus(super.getCoordinatesX(), super.getCoordinatesY(), 1);
        else if (result == 2) containedBonus = new Bonus(super.getCoordinatesX(), super.getCoordinatesY(), 2);
        else containedBonus = null;
    }

    public Bonus getContainedBonus() {
        return containedBonus;
    }

    public boolean ableToMove() {
        return (System.nanoTime() - lastTimeMove >= DURATION / level * 1.05e9);
    }

    public double getDuraionAtLevel() {
        return DURATION / level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.abs(level) % 5 + 1;
        loadImages();
    }

    public int getNextAction() {
        if (sequenceAction.isEmpty()) {
            return new Random().nextInt(4) % 4;
        }
        return sequenceAction.remove();
    }

    public void setSequenceAction(Queue<Integer> params) {
        this.sequenceAction = params;
        System.out.print(this.getIndex() + " : ");
        System.out.println(sequenceAction);
    }

    @Override
    public boolean canMove(Board board, int direction) {
        int x = super.getCoordinatesX();
        int y = super.getCoordinatesY();
        switch (direction) {
            case (2) -> y -= 1;
            case (3) -> y += 1;
            case (0) -> x -= 1;
            case (1) -> x += 1;
        }
        Piece piece;
        if ((piece = board.getAt(x, y)) == null) return true;
        return !(piece instanceof Stone) && !(piece instanceof Brick)
                && !(piece instanceof Bomb) && !(piece instanceof Bot);
    }

    /**
     * Symbol of thread is "BOT"
     */
    @Override
    public String getSymbol() {
        return "!";
    }

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
    }
}

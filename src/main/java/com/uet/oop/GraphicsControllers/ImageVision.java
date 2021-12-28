package com.uet.oop.GraphicsControllers;

import com.uet.oop.Entities.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageVision {
    private ImageView imgview;
    private Piece piece;
    private boolean isAlive = true;

    public ImageVision(Piece piece) {
        if (piece == null) return;
        this.piece = piece;
        int x = piece.getCoordinatesX() * GameController.SIZE;
        int y = piece.getCoordinatesY() * GameController.SIZE;
        imgview = new ImageView();
        imgview.setFitWidth(GameController.SIZE);
        imgview.setFitHeight(GameController.SIZE);
        imgview.setPreserveRatio(true);
        imgview.setLayoutX(x);
        imgview.setLayoutY(y);
        if (piece instanceof Bomberman) imgview.setX(7);
        setOnStanding();
    }

    public void setPiece(Piece piece, Image temp) {
        if (piece == null) return;
        this.piece = piece;
        int x = piece.getCoordinatesX() * GameController.SIZE;
        int y = piece.getCoordinatesY() * GameController.SIZE;
        imgview.setLayoutX(x);
        imgview.setLayoutY(y);
        imgview.setImage(temp);
    }

    public Piece getPiece() {
        return piece;
    }

    public void setOnStanding() {
        if (piece instanceof Bomberman p1) {
            imgview.setImage(p1.getStandingImage());
        } else if (piece instanceof Bot p2) {
            imgview.setImage(p2.getMoveImage());
        } else if (piece instanceof Brick p3) {
            imgview.setImage(p3.getStandingImage());
        } else if (piece instanceof Stone p4) {
            imgview.setImage(p4.getStandingImage());
        } else if (piece instanceof Bomb p5) {
            imgview.setImage(p5.getBombImage());
        } else if (piece instanceof Bonus p6) {
            imgview.setImage(p6.getStandingImage());
        }
        //
    }

    public void setOnMoving() {
        if (piece instanceof Bomberman p) {
            imgview.setImage(p.getMoveImage());
        }
    }

    public void setOnExplosion() {
        if (piece instanceof Bomberman p1) {
            imgview.setImage(p1.getExplosionImage());
        } else if (piece instanceof Bot p2) {
            imgview.setImage(p2.getDeadImage());
        } else if (piece instanceof Brick p3) {
            imgview.setImage(p3.getExplosionImage());
        } else if (piece instanceof Bomb p4) {
            imgview.setImage(p4.getExplorsionImage());
        }
    }

    public Image getStandingImage() {
        if (piece instanceof Bomberman p1) {
            return p1.getStandingImage();
        } else if (piece instanceof Bot p2) {
            return p2.getMoveImage();
        } else if (piece instanceof Brick p3) {
            return p3.getStandingImage();
        } else if (piece instanceof Stone p4) {
            return p4.getStandingImage();
        } else if (piece instanceof Bomb p5) {
            return p5.getBombImage();
        } else if (piece instanceof Bonus p6) {
            return p6.getStandingImage();
        }
        return null;
    }

    public Image getMoveImage() {
        if (piece instanceof Bomberman p1) {
            return p1.getMoveImage();
        } else if (piece instanceof Bot p2) {
            return p2.getMoveImage();
        } else if (piece instanceof Brick p3) {
            return p3.getStandingImage();
        } else if (piece instanceof Stone p4) {
            return p4.getStandingImage();
        } else if (piece instanceof Bomb p5) {
            return p5.getBombImage();
        } else if (piece instanceof Bonus p6) {
            return p6.getStandingImage();
        }
        return null;
    }

    public Image getExplosionImage() {
        if (piece instanceof Bomberman p1) {
            return p1.getExplosionImage();
        } else if (piece instanceof Bot p2) {
            return p2.getDeadImage();
        } else if (piece instanceof Brick p3) {
            return p3.getExplosionImage();
        } else if (piece instanceof Bomb p4) {
            return p4.getExplorsionImage();
        }
        return null;
    }

    public ImageView getImageView() {
        return imgview;
    }

    public double getLayoutX() {
        return imgview.getLayoutX();
    }

    public double getLayoutY() {
        return imgview.getLayoutY();
    }

    public void stop() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean containedBonus() {
        if (piece instanceof Bot b1) return b1.getContainedBonus() != null;
        if (piece instanceof Brick b2) return b2.getContainedBonus() != null;
        return false;
    }

    public Bonus getContainedBonus() {
        if (piece instanceof Bot b1) return b1.getContainedBonus();
        if (piece instanceof Brick b2) return b2.getContainedBonus();
        return null;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof ImageVision other)) return false;
        return other.getPiece().equals(this.piece);
    }
}

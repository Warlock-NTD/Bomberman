package com.uet.oop.GraphicsControllers;

import com.uet.oop.AiUnits.AI;
import com.uet.oop.BombermanGame;
import com.uet.oop.Entities.*;
import com.uet.oop.ProcessingUnits.MusicPlayer;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    public static final int SIZE = 30;

    private static final MusicPlayer battleMusic
            = new MusicPlayer("src/main/resources/com/uet/oop/Musics/Battle.mp3", true);
    private static final MusicPlayer explosionSound
            = new MusicPlayer("src/main/resources/com/uet/oop/Sounds/Explosion.mp3", false);
    private static final MusicPlayer bonusSound
            = new MusicPlayer("src/main/resources/com/uet/oop/Sounds/Bonus.wav", false);
    private static final MusicPlayer clockSound
            = new MusicPlayer("src/main/resources/com/uet/oop/Sounds/clock.wav", true);
    private static final MusicPlayer loadingMusic
            = new MusicPlayer("src/main/resources/com/uet/oop/Musics/Loading.mp3", true);
    private static final MusicPlayer endingMusic
            = new MusicPlayer("src/main/resources/com/uet/oop/Musics/Victory.mp3", true);
    private static final MusicPlayer defeatSound
            = new MusicPlayer("src/main/resources/com/uet/oop/Sounds/Defeat.wav", false);

    private List<ImageVision> imageVisions;
    private ImageVision bombermanImgVision;
    public Game game;
    public Bomberman bomberman;
    public List<Bot> bots;

    public double musicVolume = 1;
    public double soundVolume = 1;
    private long lastTime;

    @FXML private Pane pane;
    @FXML private Label numOfBombs;
    @FXML private Label healthPoint;
    @FXML private Label minutesLabel;
    @FXML private Label secondsLabel;
    @FXML private ImageView loadingview;

    public void show(Game game, Bomberman bomberman) {
        this.game = game;
        this.bomberman = bomberman;
        this.bots = game.getBoard().getBots();
        musicVolume = HomeController.musicVolume;
        soundVolume = HomeController.soundVolume;
        loadingMusic.play();
        setVolume(musicVolume, soundVolume);

        pane = new Pane();
        pane.setPrefSize(540, 540);
        pane.setLayoutX(180);
        pane.setLayoutY(0);
        pane.setStyle("-fx-background-color: #32cd32");

        imageVisions = new ArrayList<>();

        List<Piece> pieces = game.getBoard().getPieces();
        pieces.forEach(piece -> {
            ImageVision imgvision = new ImageVision(piece);
            imageVisions.add(imgvision);
            if (!(piece instanceof Bomberman)) pane.getChildren().add(imgvision.getImageView());
            else bombermanImgVision = imgvision;
        });

        pane.getChildren().add(bombermanImgVision.getImageView());

        ImageView leftside = new ImageView();
        leftside.setImage(new Image(new File("src/main/resources/com/uet/oop/Images/Background/leftSide.gif").toURI().toString()));
        leftside.setPreserveRatio(false);
        leftside.setFitHeight(540);
        leftside.setFitWidth(180);
        leftside.setLayoutX(0);
        leftside.setLayoutY(0);

        numOfBombs = new Label(String.valueOf(this.bomberman.getNumOfBombs()));
        numOfBombs.setFont(Font.font("Snap ITC", 40));
        numOfBombs.setLayoutX(115);
        numOfBombs.setLayoutY(305);
        numOfBombs.setTextFill(Color.BLACK);

        healthPoint = new Label(String.valueOf(this.bomberman.getHealthPoint()));
        healthPoint.setFont(Font.font("Snap ITC", 40));
        healthPoint.setLayoutX(115);
        healthPoint.setLayoutY(245);
        healthPoint.setTextFill(Color.DARKVIOLET);

        minutesLabel = new Label();
        minutesLabel.setFont(Font.font("Snap ITC", 36));
        minutesLabel.setLayoutX(15);
        minutesLabel.setLayoutY(180);

        secondsLabel = new Label();
        secondsLabel.setFont(Font.font("Snap ITC", 36));
        secondsLabel.setLayoutX(100);
        secondsLabel.setLayoutY(180);

        loadingview = new ImageView(
                new Image(new File("src/main/resources/com/uet/oop/Images/Background/loadingGame.gif").toURI().toString())
        );
        loadingview.setFitWidth(720);
        loadingview.setFitHeight(540);
        loadingview.setPreserveRatio(false);
        loadingview.setLayoutX(0);
        loadingview.setLayoutY(0);

        ImageView logo = new ImageView(
                new Image(new File("src/main/resources/com/uet/oop/Images/Background/bomberman.png").toURI().toString())
        );
        logo.setFitWidth(180);
        logo.setPreserveRatio(true);
        logo.setLayoutX(0);
        logo.setLayoutY(40);

        Pane root = new Pane();
        root.setPrefSize(720, 540);
        root.getChildren().addAll(leftside, logo, pane, numOfBombs, healthPoint, minutesLabel, secondsLabel, loadingview);
        Scene playingScene;
        playingScene = new Scene(root, 720, 540);
        playingScene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case ESCAPE -> {
                    game.pause();
                    battleMusic.pause();
                    clockSound.pause();
                    showGameSettings();
                }
                case LEFT, A -> moveBomberman(0);
                case RIGHT, D -> moveBomberman(1);
                case UP, W -> moveBomberman(2);
                case DOWN, S -> moveBomberman(3);
                case NUMPAD0, SPACE -> bomb();
            }
            setBombermanResources();
        });

        Stage stage = BombermanGame.mainStage;
        stage.setScene(playingScene);
        stage.show();
    }

    public void bomb() {
        if (bomberman.getNumOfBombs() < 1) return;
        if (bomberman.isStunned()) return;
        int x = bomberman.getCoordinatesX();
        int y = bomberman.getCoordinatesY();
        Bomb bomb = game.bombAt(x, y);
        bomberman.useBomb();
        ImageVision bombVision = new ImageVision(bomb);
        imageVisions.add(bombVision);
        pane.getChildren().add(bombVision.getImageView());
    }

    public void explore(Piece[] deadPieces) {
        int xx = deadPieces[0].getCoordinatesX();
        int yy = deadPieces[0].getCoordinatesY();
        int len = deadPieces.length;
        explosionSound.play();
        for (int i = 0; i < len; i++) {
            Piece piece = deadPieces[i];
            if (piece == null || piece instanceof Bomberman
                    || piece instanceof Bonus) {
                int finalI = i;
                Platform.runLater(() -> {
                    double lx = xx, ly = yy;
                    switch (finalI) {
                        case (1) -> ly -= 1;
                        case (2) -> lx += 1;
                        case (3) -> ly += 1;
                        case (4) -> lx -= 1;
                        case (5) -> ly -= 2;
                        case (6) -> lx += 2;
                        case (7) -> ly += 2;
                        case (8) -> lx -= 2;
                    }
                    ImageView iv = new ImageView(new Image(
                            new File("src/main/resources/com/uet/oop/Images/BombExplores/0.gif").toURI().toString()
                    ));
                    iv.setFitWidth(SIZE);
                    iv.setFitHeight(SIZE);
                    iv.setPreserveRatio(true);
                    iv.setLayoutX(lx * SIZE);
                    iv.setLayoutY(ly * SIZE);
                    pane.getChildren().add(iv);
                    animated(iv, lx * SIZE, lx * SIZE, ly * SIZE, ly * SIZE, 0.5, null);
                });
            } else {
                ImageVision imgvis = null;
                if (piece instanceof Stone) {
                    if (i <= 4) deadPieces[i + 4] = new Stone();
                    continue;
                }
                for (ImageVision iv : imageVisions) {
                    if (iv.getPiece().equals(piece)) imgvis = iv;
                }
                System.out.print(piece.getCoordinatesX() + " " + piece.getCoordinatesY() + " ");
                if (imgvis == null) continue;
                double x = imgvis.getImageView().getLayoutX();
                double y = imgvis.getImageView().getLayoutY();
                Image img = null;
                if (piece instanceof Bonus b) img = b.getStandingImage();
                imgvis.setOnExplosion();
                animated(imgvis.getImageView(), x, x, y, y, 0.5, img);
                Bonus bonus = imgvis.getContainedBonus();
                if (bonus != null) {
                    ImageVision bvis = new ImageVision(bonus);
                    System.out.println(bonus.getCoordinatesX() + " " + bonus.getCoordinatesY());
                    imageVisions.add(bvis);
                    Platform.runLater(() -> {
                        pane.getChildren().add(bvis.getImageView());
                    });
                }
            }
        }
    }

    public void moveBomberman(int direction) {
        // 0 -> left; 1 -> right; 2 -> up; 3 -> down
        if (!bomberman.ableToMove()) return;
        boolean canMove = bomberman.canMove(game.getBoard(), direction);
        game.movePiece(bomberman, direction);
        if (canMove) {
            bombermanImgVision.setOnMoving();
            double fx = bombermanImgVision.getLayoutX();
            double fy = bombermanImgVision.getLayoutY();
            double tx = bomberman.getCoordinatesX() * SIZE;
            double ty = bomberman.getCoordinatesY() * SIZE;
            animated(bombermanImgVision.getImageView(), fx, tx, fy, ty,
                    Bomberman.DURATION, bomberman.getStandingImage());

            for (Bot bot : game.getBoard().getBots()) {
                bot.setSequenceAction(AI.botAction(game.getBoard().toString(), bot.getCoordinatesX()
                        , bot.getCoordinatesY()
                        , bomberman.getCoordinatesX(), bomberman.getCoordinatesY()));
            }

        } else {
            bombermanImgVision.setOnStanding();
        }
    }

    public void bombermanDie() {
        defeatSound.play();
        bombermanImgVision.setOnExplosion();
        animated(bombermanImgVision.getImageView(), bombermanImgVision.getLayoutX(), bombermanImgVision.getLayoutX(),
                bombermanImgVision.getLayoutY(), bombermanImgVision.getLayoutY(), Bomberman.DURATION, null);
    }

    public void moveBot(Bot bot, int direction) {
        if (!bot.canMove(game.getBoard(), direction)) return;
        ImageVision imgvis = null;
        for (ImageVision iv : imageVisions) {
            if (iv.isAlive() && iv.getPiece().equals(bot)) imgvis = iv;
        }
        if (imgvis == null) return;
        double fx = imgvis.getImageView().getLayoutX();
        double fy = imgvis.getImageView().getLayoutY();
        game.movePiece(bot, direction);
        double tx = bot.getCoordinatesX() * SIZE;
        double ty = bot.getCoordinatesY() * SIZE;
        Image img = imgvis.getMoveImage();
        double duration = bot.getDuraionAtLevel();
        if (!imgvis.isAlive()) img = null;
        animated(imgvis.getImageView(), fx, tx, fy, ty, duration, img);
    }

    private void animated(ImageView imgview, double fx, double tx, double fy, double ty, double interval, Image img) {
        if (imgview == null) return;
        TranslateTransition animation = new TranslateTransition(
                Duration.seconds(interval), imgview
        );
        animation.setCycleCount(1);
        if (fx != tx) {
            animation.setFromX(0);
            animation.setToX(tx - fx);
        }
        if (fy != ty) {
            animation.setFromY(0);
            animation.setToY(ty - fy);
        }
        animation.setOnFinished(actionEvent -> {
            imgview.setLayoutX(tx);
            imgview.setLayoutY(ty);
            imgview.setTranslateY(0);
            imgview.setTranslateX(0);
            if (img == null) imgview.setVisible(false);
            else imgview.setImage(img);
        });
        animation.play();
    }

    public void fade() {
        if (bombermanImgVision.getImageView() == null) return;
        System.out.println(1);
        try {
            FadeTransition fade = new FadeTransition(
                    Duration.seconds(Bomberman.STUNNED_TIME / 6), bombermanImgVision.getImageView()
            );
            fade.setFromValue(1.0);
            fade.setToValue(0.3);
            fade.setAutoReverse(true);
            fade.setCycleCount(6);
            fade.play();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println(2);
    }

    public void claimBonus(Bonus bonus) {
        bonusSound.play();
        System.out.println("claim bonus");
        imageVisions.forEach(iv -> {
            if (iv.isAlive() && iv.getPiece().equals(bonus)) {
                animated(iv.getImageView(), iv.getLayoutX(), iv.getLayoutX(),
                        iv.getLayoutY(), iv.getLayoutY(), 0.1, null);
                iv.stop();
            }
        });
        game.getBoard().getPieces().remove(bonus);
    }

    public void setBombermanResources() {
        Platform.runLater(() -> {
            numOfBombs.setText(String.valueOf(this.bomberman.getNumOfBombs()));
            healthPoint.setText(String.valueOf(this.bomberman.getHealthPoint()));
        });
    }

    public void showGameSettings() {
        new GamesSettingsController().show(this);
    }

    public void musicOn() {
        battleMusic.play();
    }

    public void setRemainingTime(long time) {
        int m = ((int) time) / 60;
        int s = ((int) time) % 60;
        if (System.currentTimeMillis() - lastTime < 1e3) return;
        lastTime = System.currentTimeMillis();
        Platform.runLater(() -> {
            String ms = String.valueOf(m);
            if (m < 10) ms = "0" + ms;
            minutesLabel.setText(ms);
            String ss = String.valueOf(s);
            if (s < 10) ss = "0" + ss;
            secondsLabel.setText(ss);
            if (m == 0 && s <= 3) {
                minutesLabel.setTextFill(Color.RED);
                secondsLabel.setTextFill(Color.RED);
                clockSound.play();
                clockSound.setVolume(soundVolume);
                if (s == 0) {
                    clockSound.stop();
                    defeatSound.play();
                }
            } else {
                minutesLabel.setTextFill(Color.LIMEGREEN);
                secondsLabel.setTextFill(Color.LIMEGREEN);
                clockSound.pause();
            }
        });
    }

    public void loadingDone() {
        loadingview.setVisible(false);
        loadingMusic.stop();
        battleMusic.play();
    }

    public void setVolume(double mv, double sv) {
        musicVolume = mv;
        battleMusic.setVolume(musicVolume);
        soundVolume = sv;
        explosionSound.setVolume(soundVolume);
        bonusSound.setVolume(soundVolume);
        clockSound.setVolume(soundVolume);
        loadingMusic.setVolume(musicVolume);
    }

    public void setEnding(boolean win) {
        System.out.println("ending");
        battleMusic.stop();
        Platform.runLater(() -> {
            try {
                Pane pane = new Pane();
                pane.setPrefSize(720, 540);

                ImageView imgview = new ImageView();
                imgview.setFitWidth(720);
                imgview.setFitHeight(540);
                imgview.setPreserveRatio(false);
                if (win)
                    imgview.setImage(new Image(
                            new File("src/main/resources/com/uet/oop/Images/Background/victory.gif").toURI().toString()));
                else
                    imgview.setImage(new Image(
                            new File("src/main/resources/com/uet/oop/Images/Background/defeat.gif").toURI().toString()));
                endingMusic.play();

                pane.getChildren().add(imgview);
                Scene scene = new Scene(pane, 720, 540);
                scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
                    System.out.println(keyEvent.getCode());
                    endingMusic.stop();
                    new HomeController().show();
                });
                Stage stage = BombermanGame.mainStage;
                stage.setScene(scene);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }
}

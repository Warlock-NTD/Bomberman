package com.uet.oop.GraphicsControllers;

import com.uet.oop.ProcessingUnits.MusicPlayer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;

public class GamesSettingsController {
    private static final MusicPlayer musicPlayer = new MusicPlayer("src/main/resources/com/uet/oop/Musics/Settings.mp3", true);
    private static final MusicPlayer selectSound = new MusicPlayer("src/main/resources/com/uet/oop/Sounds/Select.wav", false);

    private double musicVolume = 1;
    private double soundVolume = 1;

    private GameController gc;

    @FXML private ImageView logo;
    @FXML private ImageView musicIcon;
    @FXML private ImageView soundIcon;
    @FXML private Slider musicSlider;
    @FXML private Slider soundSlider;
    @FXML private ImageView resumeButton;
    @FXML private ImageView exitButton;
    @FXML private Stage stage;

    @FXML
    private void musicSliderOnDragged() {
        double value = musicSlider.getValue();
        if (value == 0) {
            musicIcon.setImage(new Image(new File("src//main//resources//com//uet//oop//Images//Background//muteMusic.png").toURI().toString()));
            musicPlayer.setVolume(0);
        } else {
            musicIcon.setImage(new Image(new File("src//main//resources//com//uet//oop//Images//Background//music.png").toURI().toString()));
            musicPlayer.setVolume(value);
            musicVolume = value;
        }
        gc.setVolume(musicSlider.getValue(), soundSlider.getValue());
    }

    @FXML
    private void muteMusic() {
        selectSound.play();
        if (musicSlider.getValue() > 0) {
            musicIcon.setImage(new Image(new File("src//main//resources//com//uet//oop//Images//Background//muteMusic.png").toURI().toString()));
            musicSlider.setValue(0);
            musicPlayer.setVolume(0);
        } else {
            musicIcon.setImage(new Image(new File("src//main//resources//com//uet//oop//Images//Background//music.png").toURI().toString()));
            musicSlider.setValue(musicVolume);
            musicPlayer.setVolume(musicVolume);
        }
        gc.setVolume(musicSlider.getValue(), soundSlider.getValue());
    }

    @FXML
    private void soundSliderOnDragged() {
        double value = soundSlider.getValue();
        if (value == 0) {
            soundIcon.setImage(new Image(new File("src//main//resources//com//uet//oop//Images//Background//muteSound.png").toURI().toString()));
            selectSound.setVolume(0);
        } else {
            soundIcon.setImage(new Image(new File("src//main//resources//com//uet//oop//Images//Background//sound.png").toURI().toString()));
            selectSound.setVolume(value);
            soundVolume = value;
        }
        gc.setVolume(musicSlider.getValue(), soundSlider.getValue());
    }

    @FXML
    private void muteSound() {
        selectSound.play();
        if (soundSlider.getValue() > 0) {
            soundIcon.setImage(new Image(new File("src//main//resources//com//uet//oop//Images//Background//muteSound.png").toURI().toString()));
            soundSlider.setValue(0);
            selectSound.setVolume(0);
        } else {
            soundIcon.setImage(new Image(new File("src//main//resources//com//uet//oop//Images//Background//sound.png").toURI().toString()));
            soundSlider.setValue(soundVolume);
            selectSound.setVolume(soundVolume);
        }
        gc.setVolume(musicSlider.getValue(), soundSlider.getValue());
    }

    @FXML
    private void resumeGame() {
        selectSound.play();
        musicPlayer.stop();
        gc.musicOn();
        gc.game.resume();
        stage.close();
    }

    @FXML
    private void exitGame() {
        selectSound.play();
        musicPlayer.stop();
        gc.game.stop();
        stage.close();
        new HomeController().show();
    }

    public void show(GameController gc) {
        this.gc = gc;
        stage = new Stage();
        musicPlayer.play();
        musicPlayer.setVolume(gc.musicVolume);
        selectSound.setVolume(gc.soundVolume);
        try {
            logo = new ImageView(
                    new Image(new File("src/main/resources/com/uet/oop/Images/Background/bomberman.png").toURI().toString())
            );
            logo.setFitWidth(300);
            logo.setFitHeight(97);
            logo.setLayoutX(0);
            logo.setLayoutY(0);
            logo.setPreserveRatio(true);

            resumeButton = new ImageView(
                    new Image(new File("src/main/resources/com/uet/oop/Images/Background/resume.png").toURI().toString())
            );
            resumeButton.setPreserveRatio(true);
            resumeButton.setFitWidth(137);
            resumeButton.setFitHeight(54);
            resumeButton.setLayoutX(95);
            resumeButton.setLayoutY(205);
            resumeButton.setOnMouseClicked(mouseEvent -> {
                resumeGame();
            });

            exitButton = new ImageView(
                    new Image(new File("src/main/resources/com/uet/oop/Images/Background/exit.png").toURI().toString())
            );
            exitButton.setPreserveRatio(true);
            exitButton.setFitWidth(137);
            exitButton.setFitHeight(54);
            exitButton.setLayoutX(95);
            exitButton.setLayoutY(268);
            exitButton.setOnMouseClicked(mouseEvent -> {
                exitGame();
            });

            musicSlider = new Slider();
            musicSlider.setMax(1);
            musicSlider.setValue(gc.musicVolume);
            musicSlider.setPrefSize(186, 14);
            musicSlider.setLayoutX(85);
            musicSlider.setLayoutY(114);
            musicSlider.setOnMouseClicked(mouseEvent -> {
                musicSliderOnDragged();
            });
            musicSlider.setOnMouseDragged(mouseEvent -> {
                musicSliderOnDragged();
            });

            soundSlider = new Slider();
            soundSlider.setMax(1);
            soundSlider.setValue(gc.soundVolume);
            soundSlider.setPrefSize(186, 14);
            soundSlider.setLayoutX(85);
            soundSlider.setLayoutY(159);
            soundSlider.setOnMouseClicked(mouseEvent -> {
                soundSliderOnDragged();
            });
            soundSlider.setOnMouseDragged(mouseEvent -> {
                soundSliderOnDragged();
            });

            soundIcon = new ImageView(
                    new Image(new File("src//main//resources//com//uet//oop//Images//Background//sound.png").toURI().toString())
            );
            soundIcon.setPreserveRatio(true);
            soundIcon.setFitWidth(31);
            soundIcon.setFitHeight(32);
            soundIcon.setLayoutX(30);
            soundIcon.setLayoutY(153);
            soundIcon.setOnMouseClicked(mouseEvent -> {
                muteSound();
            });

            musicIcon = new ImageView(
                    new Image(new File("src//main//resources//com//uet//oop//Images//Background//music.png").toURI().toString())
            );
            musicIcon.setPreserveRatio(true);
            musicIcon.setFitWidth(31);
            musicIcon.setFitHeight(32);
            musicIcon.setLayoutX(31);
            musicIcon.setLayoutY(105);
            musicIcon.setOnMouseClicked(mouseEvent -> {
                muteMusic();
            });

            Pane root = new Pane(logo, musicIcon, musicSlider, soundIcon, soundSlider, resumeButton, exitButton);
            root.setStyle("-fx-background-color: #e9967a");

            Scene scene = new Scene(root, 300, 350);
            stage.setTitle("Bomberman Go: Settings");
            stage.getIcons().add(
                    new Image(new File("src/main/resources/com/uet/oop/Images/Background/Icon.png").toURI().toString())
            );
            stage.setOnCloseRequest(windowEvent -> {
                System.out.println("resume");
                resumeGame();
            });
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

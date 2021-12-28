package com.uet.oop.GraphicsControllers;

import com.uet.oop.BombermanGame;
import com.uet.oop.Entities.Bomberman;
import com.uet.oop.Entities.Game;
import com.uet.oop.ProcessingUnits.MusicPlayer;
import com.uet.oop.ProcessingUnits.GameRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

public class HomeController {
    @FXML private ImageView map;
    @FXML private ImageView bomberman;
    @FXML private Slider musicSlider;
    @FXML private Slider soundSlider;
    @FXML private ImageView musicIcon;
    @FXML private ImageView soundIcon;
    @FXML private Label status;

    public static int HIGHEST_LEVEL = 1;
    public static int SELECTED_LEVEL = 1;
    private boolean sttCheck = true;

    private static final List<Image> bombermanImage = new ArrayList<>(Arrays.asList(
            new Image(new File("src/main/resources/com/uet/oop/Images/Background/1.gif").toURI().toString()),
            new Image(new File("src/main/resources/com/uet/oop/Images/Background/2.gif").toURI().toString()),
            new Image(new File("src/main/resources/com/uet/oop/Images/Background/3.gif").toURI().toString()),
            new Image(new File("src/main/resources/com/uet/oop/Images/Background/4.gif").toURI().toString())
    ));
    private static List<Image> mapImages = new ArrayList<>();
    private int imageIndex = 0;
    private static final MusicPlayer musicPlayer = new MusicPlayer("src/main/resources/com/uet/oop/Musics/Select.mp3", true);
    private static final MusicPlayer selectSound = new MusicPlayer("src/main/resources/com/uet/oop/Sounds/Select.wav", false);
    private List<ImageView> images;
    private int mapIndex = 0;
    public static double musicVolume = 1;
    public static double soundVolume = 1;

    @FXML
    private void previousMap() {
        selectSound.play();
        mapIndex--;
        if (mapIndex < 0) mapIndex += mapImages.size();
        map.setImage(mapImages.get(mapIndex));
        sttCheck = (mapIndex <= HIGHEST_LEVEL - 1);
        if (!sttCheck) {
            status.setText("Lock");
            status.setTextFill(Color.RED);
        } else {
            status.setText("Unlock");
            status.setTextFill(Color.GREEN);
        }
        System.out.println(mapIndex + " " + HIGHEST_LEVEL);
    }

    @FXML
    private void nextMap() {
        selectSound.play();
        mapIndex++;
        mapIndex = mapIndex % mapImages.size();
        map.setImage(mapImages.get(mapIndex));
        sttCheck = (mapIndex <= HIGHEST_LEVEL - 1);
        if (!sttCheck) {
            status.setText("Lock");
            status.setTextFill(Color.RED);
        } else {
            status.setText("Unlock");
            status.setTextFill(Color.GREEN);
        }
        System.out.println(mapIndex + " " + HIGHEST_LEVEL);
    }

    @FXML
    private void previousBomberman() {
        selectSound.play();
        imageIndex--;
        if (imageIndex < 0) imageIndex += bombermanImage.size();
        bomberman.setImage(bombermanImage.get(imageIndex));
    }

    @FXML
    private void nextBomberman() {
        selectSound.play();
        imageIndex++;
        imageIndex = imageIndex % 4;
        bomberman.setImage(bombermanImage.get(imageIndex));
    }

    @FXML
    private void playGame() {
        musicVolume = musicPlayer.getVolume();
        soundVolume = selectSound.getVolume();

        selectSound.play();

        if (!sttCheck) return;
        SELECTED_LEVEL = mapIndex + 1;

        musicPlayer.stop();
        selectSound.stop();

        Game game = new Game();
        String mp = "map_" + (SELECTED_LEVEL) + ".txt";
//        String mp = "map1.txt";
        game.initialize("src/main/resources/com/uet/oop/Maps/" + mp);
        Bomberman bomberman = game.getBoard().getBomberman();
        switch(imageIndex) {
            case (0) -> bomberman.setColor("Yellow");
            case (1) -> bomberman.setColor("Green");
            case (2) -> bomberman.setColor("Red");
            case (3) -> bomberman.setColor("Blue");
        }
        GameController gc = new GameController();
        GameRunner rg = new GameRunner(gc);
        gc.show(game, bomberman);
        rg.start();
    }

    @FXML
    private void quitGame() {
        selectSound.play();
        musicPlayer.stop();
        selectSound.stop();
        new BombermanGame().start(BombermanGame.mainStage);
    }

    @FXML
    private void muteMusic() {
        selectSound.play();
        if (musicSlider.getValue() > 0) {
            musicIcon.setImage(new Image(new File("src/main/resources/com/uet/oop/Images/Background/muteMusic.png").toURI().toString()));
            musicSlider.setValue(0);
            musicPlayer.setVolume(0);
        } else {
            musicIcon.setImage(new Image(new File("src/main/resources/com/uet/oop/Images/Background/music.png").toURI().toString()));
            musicSlider.setValue(musicVolume);
            musicPlayer.setVolume(musicVolume);
        }
    }

    @FXML
    private void muteSound() {
        selectSound.play();
        if (soundSlider.getValue() > 0) {
            soundIcon.setImage(new Image(new File("src/main/resources/com/uet/oop/Images/Background/muteSound.png").toURI().toString()));
            soundSlider.setValue(0);
            selectSound.setVolume(0);
        } else {
            soundIcon.setImage(new Image(new File("src/main/resources/com/uet/oop/Images/Background/sound.png").toURI().toString()));
            soundSlider.setValue(soundVolume);
            selectSound.setVolume(soundVolume);
        }
    }

    @FXML
    private void musicSliderOnDragged() {
        double value = musicSlider.getValue();
        if (value == 0) {
            musicIcon.setImage(new Image(new File("src/main/resources/com/uet/oop/Images/Background/muteMusic.png").toURI().toString()));
            musicPlayer.setVolume(0);
        } else {
            musicIcon.setImage(new Image(new File("src/main/resources/com/uet/oop/Images/Background/music.png").toURI().toString()));
            musicPlayer.setVolume(value);
            musicVolume = value;
        }
    }

    @FXML
    private void soundSliderOnDragged() {
        double value = soundSlider.getValue();
        if (value == 0) {
            soundIcon.setImage(new Image(new File("src/main/resources/com/uet/oop/Images/Background/muteSound.png").toURI().toString()));
            selectSound.setVolume(0);
        } else {
            soundIcon.setImage(new Image(new File("src/main/resources/com/uet/oop/Images/Background/sound.png").toURI().toString()));
            selectSound.setVolume(value);
            soundVolume = value;
        }
    }

    public void show() {
        musicPlayer.play();
        String p = "src/main/resources/com/uet/oop/Images/Maps/map";
        for (int i = 1; i <= 10; i++) {
            mapImages.add(
                new Image(new File(p + i + ".png").toURI().toString())
            );
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BombermanGame.class.getResource("FXML/Home.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 720, 540);
            Stage stage = BombermanGame.mainStage;
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void setImages(List<ImageView> list) {
        images = list;
    }

    public List<ImageView> getImages() {
        return images;
    }
}

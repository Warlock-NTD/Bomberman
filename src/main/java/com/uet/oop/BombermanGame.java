package com.uet.oop;

import com.uet.oop.GraphicsControllers.HomeController;
import com.uet.oop.ProcessingUnits.MusicPlayer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

public class BombermanGame extends Application {
    public static Stage mainStage;
    private static final MusicPlayer musicPlayer =
            new MusicPlayer("src//main//resources//com//uet//oop//Musics//Title.mp3", true);

    @Override
    public void start(Stage primaryStage) {
        musicPlayer.play();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML/Introduction.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 720, 540);
            primaryStage.setTitle("Bomberman Go");
            primaryStage.getIcons().add(
                    new Image(new File("src/main/resources/com/uet/oop/Images/Background/Icon.png").toURI().toString())
            );
            primaryStage.setScene(scene);
            mainStage = primaryStage;
            primaryStage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        mainStage.setOnCloseRequest(windowEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bomberman Go: Quit");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(
                    new Image(new File("src/main/resources/com/uet/oop/Images/Background/Icon.png").toURI().toString())
            );
            alert.setHeaderText("Quit game?");
            alert.setContentText("Continue");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.isPresent() && action.get() == ButtonType.OK) {
                musicPlayer.stop();
                BombermanGame.mainStage.close();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                try {
                    File file = new File("src/main/resources/com/uet/oop/PlayerRecord/PlayerRecords");
                    Scanner sc = new Scanner(file);
                    StringBuilder recs = new StringBuilder();
                    while (sc.hasNextLine()) {
                        recs.append(sc.nextLine()).append("\n");
                    }
                    recs.append(dateFormat.format(date)).append(": Level passed ").append(HomeController.HIGHEST_LEVEL);
                    sc.close();

                    FileWriter fw = new FileWriter(file);
                    fw.write(recs.toString());
                    fw.close();

                    File f = new File("src/main/resources/com/uet/oop/PlayerRecord/Level.txt");
                    FileWriter fw2 = new FileWriter(f);
                    fw2.append(String.valueOf(HomeController.HIGHEST_LEVEL));
                    fw2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

    @FXML
    private void playButtonOnClicked() {
        musicPlayer.stop();
        try {
            Scanner sc = new Scanner(new File("src/main/resources/com/uet/oop/PlayerRecord/Level.txt"));
            HomeController.HIGHEST_LEVEL = sc.nextInt();
            System.out.println(HomeController.HIGHEST_LEVEL);
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new HomeController().show();
    }

    public static void main(String[] args) {
        launch();
    }
}

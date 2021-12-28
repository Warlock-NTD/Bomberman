package com.uet.oop.ProcessingUnits;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean repeat;
    private String path;

    public MusicPlayer(String path, boolean repeat) {
        File file = new File(path);
        if (!file.exists()) System.err.println("File not found!");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        this.repeat = repeat;
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                if (!repeat) mediaPlayer.pause();
            }
        });
    }

    public MusicPlayer(String path, boolean repeat, double volume) {
        File file = new File(path);
        if (!file.exists()) System.err.println("File not found!");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        this.repeat = repeat;
        if (repeat) {
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                }
            });
        }
        setVolume(volume);
    }

    public MusicPlayer() {

    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                if (!repeat) mediaPlayer.pause();
            }
        });
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setVolume(double volume) {
        if (volume < 0) volume = 0;
        if (volume > 1) volume = 1;
        mediaPlayer.setVolume(volume);
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    public void play() {
        if (!repeat) mediaPlayer.seek(mediaPlayer.getStartTime());
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) return;
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)) return;
        mediaPlayer.pause();
    }
}

package com.uet.oop.Entities;

import com.uet.oop.GraphicsControllers.HomeController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Board {
    private static List<Piece> pieces;
    public static final int SIZE = 18;
    private int playingTime;

    public Board() {
        pieces = new ArrayList<>();
    }

    void readBoard(String path) {
        Piece.AUTO_INCREMENT_INDEX = 0;
        try {
            File file = new File(path);
            if (!file.exists()) System.err.println("File not found");
            Scanner sc = new Scanner(file);
            playingTime = sc.nextInt();
            System.out.println(playingTime + "\n" + SIZE);
            sc.nextLine();
            for (int j = 0; j < SIZE && sc.hasNextLine(); j++) {
                String s = sc.nextLine();
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '#') {
                        pieces.add(new Stone(i, j));
                    } else if (s.charAt(i) == '=') {
                        pieces.add(new Brick(i, j));
                    } else if (s.charAt(i) == '!') {
                        pieces.add(new Bot(i, j, new Random().nextInt(HomeController.HIGHEST_LEVEL) % 5 + 1));
                    } else if (s.charAt(i) == '$') {
                        pieces.add(new Bomberman(i, j));
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addBonusTime(int time) {
        playingTime += time;
    }

    public double getPlayingTime() {
        return this.playingTime;
    }

    public Piece getAt(int x, int y) {
        if (pieces.isEmpty()) return null;
        for (Piece piece : pieces) {
            if (piece != null && piece.checkPosition(x, y)) {
                return piece;
            }
        }
        return null;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void add(Piece piece) {
        if (piece == null) return;
        pieces.add(piece);
    }

    public List<Piece> getAllAt(int x, int y) {
        List<Piece> res = new ArrayList<>();
        Piece piece;
        for (int i = 0; i < pieces.size(); i++) {
            if ((piece = pieces.get(i)) != null && piece.checkPosition(x, y)) res.add(piece);
        }
        return res;
    }

    public void remove(Piece piece) {
        if (piece instanceof Bomberman) return;
        if (piece instanceof Bonus) return;
        if (piece instanceof Bomb b) {
            b.explore();
            pieces.remove(b);
        }
        if (piece instanceof Bot b1) {
            Bonus b;
            if ((b = b1.getContainedBonus()) != null) {
                b.setCoordinatesX(b1.getCoordinatesX());
                b.setCoordinatesY(b1.getCoordinatesY());
                pieces.add(b);
            }
        }
        else if (piece instanceof Brick b2) {
            Bonus b;
            if ((b = b2.getContainedBonus()) != null){
                b.setCoordinatesX(b2.getCoordinatesX());
                b.setCoordinatesY(b2.getCoordinatesY());
                pieces.add(b2.getContainedBonus());
            }
        }
        pieces.remove(piece);
    }

    public void print() {
        for (int i = 0; i < 30; i++) System.out.println();
        System.out.println(this);
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        String[][] board = new String[SIZE][SIZE];
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if (piece instanceof Bot) continue;
            if (piece instanceof Bomberman) continue;
            board[piece.getCoordinatesY()][piece.getCoordinatesX()] = piece.getSymbol();
        }
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                if (board[j][i] == null) res.append(" ");
                else res.append(board[j][i]);
            }
            res.append("\n");
        }
        return res.substring(0, res.length() - 1);
    }

    public List<Bot> getBots() {
        List<Bot> bots = new ArrayList<>();
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if (piece != null && piece instanceof Bot) {
                bots.add((Bot) piece);
            }
        }
        return bots;
    }

    public Bomberman getBomberman() {
        for (Piece piece : pieces) {
            if (piece instanceof Bomberman) {
                return (Bomberman) piece;
            }
        }
        return null;
    }

    public List<Bomb> getBombs() {
        List<Bomb> bombs = new ArrayList<>();
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if (piece != null && piece instanceof Bomb) {
                bombs.add((Bomb) piece);
            }
        }
        return bombs;
    }
}

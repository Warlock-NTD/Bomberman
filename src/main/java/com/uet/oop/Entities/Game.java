package com.uet.oop.Entities;

public class Game {
    private static Board board;
    private boolean isRunning;
    private boolean isPaused;
    private long startTime;
    private int endingStatus;
    private long pausedTime;
    private double penalty = 0;

    public Game() {

    }

    public void setStatus(int stt) {
        endingStatus = stt;
        System.out.println(stt);
        if (stt == 1) isRunning = false;
    }

    public int getEndingStatus() {
        return endingStatus;
    }

    public boolean isRunning() {
        isRunning = (!isTimedOut() && isRunning);
        return isRunning;
    }

    public void run() {
        startTime = System.currentTimeMillis();
        isRunning = true;
    }

    /**
     * get remaining time of the match
     * @return time.seconds;
     */
    public long getRemainingTime() {
        double playedTime = (System.currentTimeMillis() - startTime - penalty) / 1e3;
        long rt = (long) ((long) board.getPlayingTime() - playedTime);
        return rt;
    }

    public boolean isTimedOut() {
        return (System.currentTimeMillis() - startTime - penalty) >= (board.getPlayingTime() * 1e3);
    }

    public void stop() {
        endingStatus = -1;
        isRunning = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void pause() {
        isPaused = true;
        pausedTime = System.currentTimeMillis();
    }

    public void resume() {
        isPaused = false;
        penalty += (System.currentTimeMillis() - pausedTime) / 1e3;
    }

    public void bonusTime() {
        board.addBonusTime(Bonus.TIME_BONUS);
    }

    public void initialize(String boardFile) {
        board = new Board();
        board.readBoard(boardFile);
    }

    public boolean validate(int x, int y) {
        return (x >= 1 && x < Board.SIZE - 1 && y >= 1 && y < Board.SIZE - 1);
    }

    public void movePiece(Piece piece, int direction) {
        if (piece == null) return;
        if (piece instanceof Stone) return;
        if (piece instanceof Brick) return;
        if (piece instanceof Bomb) return;
        if (piece.canMove(board, direction)) {
            piece.move(direction);
        }
    }

    public Bomb bombAt(int x, int y) {
        if (!validate(x, y)) return null;
        Piece piece = board.getAt(x, y);
        if (piece instanceof Stone) return null;
        if (piece instanceof Brick) return null;
        Bomb bomb = new Bomb(x, y);
        board.add(bomb);
        bomb.startCountingDown();
        return bomb;
    }

    public Piece[] explore(Bomb bomb) {
        Piece[] res = new Piece[9];

        int x = bomb.getCoordinatesX();
        int y = bomb.getCoordinatesY();

        res[0] = bomb;
        res[1] = board.getAt(x, y - 1);
        res[2] = board.getAt(x + 1, y);
        res[3] = board.getAt(x, y + 1);
        res[4] = board.getAt(x - 1, y);
        res[5] = board.getAt(x, y - 2);
        res[6] = board.getAt(x + 2, y);
        res[7] = board.getAt(x, y + 2);
        res[8] = board.getAt(x - 2, y);

        board.remove(res[0]);

        if (!(res[1] instanceof Stone)) {
            board.remove(res[1]);
            if (!(res[5] instanceof Stone)) board.remove(res[5]);
        }
        if (!(res[2] instanceof Stone)) {
            board.remove(res[2]);
            if (!(res[6] instanceof Stone)) board.remove(res[6]);
        }
        if (!(res[3] instanceof Stone)) {
            board.remove(res[3]);
            if (!(res[7] instanceof Stone)) board.remove(res[7]);
        }
        if (!(res[4] instanceof Stone)) {
            board.remove(res[4]);
            if (!(res[8] instanceof Stone)) board.remove(res[8]);
        }

        return res;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board newBoard) {
        board = newBoard;
    }
}

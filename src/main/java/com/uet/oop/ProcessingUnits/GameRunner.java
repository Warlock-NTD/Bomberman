package com.uet.oop.ProcessingUnits;

import com.uet.oop.AiUnits.AI;
import com.uet.oop.Entities.Bomb;
import com.uet.oop.Entities.Bonus;
import com.uet.oop.Entities.Bot;
import com.uet.oop.Entities.Piece;
import com.uet.oop.GraphicsControllers.GameController;
import com.uet.oop.GraphicsControllers.HomeController;


import java.util.List;
import java.util.Random;

public class GameRunner implements Runnable {
    private Thread thread;
    private GameController gc;

    public GameRunner(GameController gc) {
        thread = new Thread(this, "RUNNING_GAME");
        thread.setDaemon(true);
        this.gc = gc;
    }

    @Override
    public void run() {
        long waiting = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - waiting >= 3e3) break;
        }
        gc.loadingDone();
        System.out.println("On playing");
        //
        gc.game.run();

        for (Bot bot : gc.game.getBoard().getBots()) {
            bot.setSequenceAction(AI.botAction(gc.game.getBoard().toString(), bot.getCoordinatesX()
                    , bot.getCoordinatesY()
                    , gc.bomberman.getCoordinatesX(), gc.bomberman.getCoordinatesY()));
        }

        while (gc.bomberman.isAlive() && gc.game.isRunning()) {
            if (gc.game.isPaused()) {
                System.out.println(1);
                continue;
            }
            //
            gc.setRemainingTime(gc.game.getRemainingTime());
            // bombs explore
            List<Bomb> bombs = gc.game.getBoard().getBombs();
            if (!bombs.isEmpty()) {
                for (Bomb bomb : bombs) {
                    if (bomb.isTimedOut() && !bomb.isExploded()) {
                        System.out.println(bomb.getIndex());
                        gc.explore(gc.game.explore(bomb));
                        if (gc.bomberman.isInExplosionRangeOf(bomb, gc.game.getBoard())) {
                            gc.bomberman.bleed();
                            gc.fade();
                            gc.setBombermanResources();
                        }
                    }
                }
            }
            gc.bomberman.restoreBomb();
            // check
            List<Piece> pieces = gc.game.getBoard().getAllAt(gc.bomberman.getCoordinatesX(), gc.bomberman.getCoordinatesY());
            if (!pieces.isEmpty()) {
                for (Piece p : pieces) {
                    if (p instanceof Bot && !gc.bomberman.isStunned()) {
                        gc.bomberman.bleed();
                        gc.fade();
                        gc.setBombermanResources();
                        break;
                    }
                    else if (p instanceof Bonus b) {
                        switch (b.getType()) {
                            case (0) -> gc.bomberman.equipBomb();
                            case (1) -> gc.bomberman.heal();
                            case (2) -> gc.game.bonusTime();
                        }
                        gc.claimBonus(b);
                        gc.setBombermanResources();
                    }
                }
            }
            if (!gc.bomberman.isAlive()) {
                gc.bombermanDie();
                gc.game.setStatus(0);
                continue;
            }
            // bots move
            List<Bot> bots = gc.game.getBoard().getBots();
            Random random = new Random();
            if (!bots.isEmpty()) {
                for (Bot bot : bots) {
                    if (!bot.ableToMove() || random.nextInt(2) == 0) continue;
                    int direction = bot.getNextAction();
                    if (direction != -1)
                        System.out.println(bot.getIndex() + " : " + direction);
                    gc.moveBot(bot, direction);
                }
            } else gc.game.setStatus(1);
        }
        // match ending
        waiting = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - waiting < 1e3) continue;
            if (gc.game.getEndingStatus() == 1) {
                if (HomeController.SELECTED_LEVEL >= HomeController.HIGHEST_LEVEL) HomeController.HIGHEST_LEVEL++;
                gc.setEnding(true);
            } else if (gc.game.getEndingStatus() == 0) gc.setEnding(false);
            break;
        }
    }

    public void start() {
        if (thread.isAlive()) return;
        thread.start();
    }
}

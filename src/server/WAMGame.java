package server;
/**
 * needs to check game won, make move, tied game etc
 */

import client.gui.WAMBoard;

public class WAMGame implements Runnable {

    private WAMPlayer[] players;
    private Mole[] moles;
    private int rows;
    private int cols;
    private int duration;

    /** the game model */
    private WAM game;

    /**
     * Initialize the game.
     *
     * @param
     */
    public WAMGame(int rows, int cols, int duration, WAMPlayer... players)
    {
        this.duration = duration;
        this.players = players;
        this.rows = rows;
        this.cols = cols;
        this.moles = new Mole[rows*cols];
        this.game = new WAM(rows, cols);
    }

    @Override
    public void run()
    {
        // time by craig
        System.out.println("Game started");
        for (int i=0; i < rows*cols; i++) {
            Mole mole = new Mole(i, players);
            moles[i] = mole;
            mole.start();
        }
        synchronized(this) {
            try {
                wait(duration * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Game over");
        for (Mole mole : moles) {
            mole.stopGame();
        }

    }
}

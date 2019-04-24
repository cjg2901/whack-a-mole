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

    /** the game model */
    private WAMBoard game;

    /**
     * Initialize the game.
     *
     * @param
     */
    public WAMGame(int rows, int cols, WAMPlayer... players) {

        this.players = players;
        this.rows = rows;
        this.cols = cols;
        this.moles = new Mole[rows*cols];
        this.game = new WAMBoard(rows, cols, players.length);
    }

    @Override
    public void run() {
        System.out.println("Game started");
        for (int i=0; i < rows*cols; i++) {
            Mole mole = new Mole(i, players);
            moles[i] = mole;
            mole.start();
        }
    }
}

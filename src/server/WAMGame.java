package server;

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
    }

    @Override
    public void run() {
        System.out.println("Game started");
        for (int i=0; i < rows*cols; i++) {
            Mole mole = new Mole(i, players);
            moles[i] = mole;
            new Thread(mole).start();
        }
    }
}

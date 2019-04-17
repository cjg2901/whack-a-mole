package server;

import client.gui.WAMBoard;

public class WAMGame implements Runnable {

    private WAMPlayer[] players;

    /** the game model */
    private WAMBoard game;

    /**
     * Initialize the game.
     *
     * @param
     */
    public WAMGame(WAMPlayer... players) {

        this.players = players;
    }

    @Override
    public void run() {
        for (WAMPlayer player : players) {
            player.moleUp();
        }
    }
}

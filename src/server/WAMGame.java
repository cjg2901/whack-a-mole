package server;

import common.WAMException;

/**
 * Represents the entire game on the server side.
 *
 * @author Craig Gebo @ cjg2901@rit.edu
 * @author srikamal @kvc9128@g.rit.edu
 */

public class WAMGame implements Runnable {

    /** List of all players */
    private WAMPlayer[] players;
    /** List of all moles */
    private Mole[] moles;
    /** Number of rows in the board */
    private int rows;
    /** Number of columns in the board */
    private int cols;
    /** The duration of the game in seconds */
    private int duration;

    /** the game model */
    private WAM game;

    /**
     * Creates a new game.
     *
     * @param rows number of rows in the board
     * @param cols number of columns in the board
     * @param duration duration of the game in seconds
     * @param players list of all players
     */
    public WAMGame(int rows, int cols, int duration, WAMPlayer... players)
    {
        this.duration = duration;
        this.players = players;
        this.rows = rows;
        this.cols = cols;
        this.moles = new Mole[rows*cols];
        this.game = new WAM(rows, cols, players);
    }

    /**
     * Starts the game, waits for the duration of the game,
     * after the game is over it stops all of the moles.
     */
    @Override
    public void run()
    {
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
        for (WAMPlayer player : players){
            player.close();
        }
    }

    /**
     *
     * @throws WAMException
     */
    public void GameStat() throws WAMException
    {
        if (game.hasTied())
        {
            for (WAMPlayer player:players)
            {
                player.gameTied();
            }
        }
        else
        {
            WAMPlayer player = game.hasWON();
            player.gameWon();
            for (WAMPlayer other_player: players)
            {
                if(other_player != player)
                {
                    other_player.gameLost();
                }
            }
        }
    }

}

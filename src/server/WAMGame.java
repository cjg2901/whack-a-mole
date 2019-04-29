package server;

/**
 * Represents the entire game on the server side.
 *
 * @author Craig Gebo
 * @author Sri Kamal
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
     * Gets the WAM class which represents the game.
     *
     * @return the game
     */
    public WAM getGame()
    {
        return this.game;
    }

    /**
     * Starts the game, waits for the duration of the game,
     * after the game is over it stops all of the moles.
     */
    @Override
    public void run() {
        System.out.println("Game started");
        for (int i=0; i < rows*cols; i++) {
            Mole mole = new Mole(i, players, game);
            moles[i] = mole;
            mole.start();
        }
        synchronized(this) {
            try {
                wait(duration * 1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        gameStatus();
        System.out.println("Game over");
        for (Mole mole : moles) {
            mole.stopGame();
        }
        for (WAMPlayer player: players) {
            player.stopGame();
            player.close();
        }
    }

    /**
     * Checks the final status of the game, which player won and
     * which has lost or if there was a tie.
     */
    public void gameStatus()
    {
        if (game.hasTied())
        {
            for (WAMPlayer player : players)
            {
                player.gameTied();
            }
        }
        else
        {
            WAMPlayer player = game.hasWon();
            player.gameWon();
            for (WAMPlayer otherPlayer: players)
            {
                if(otherPlayer != player)
                {
                    otherPlayer.gameLost();
                }
            }
        }
    }

}

package client.gui;

import java.util.LinkedList;
import java.util.List;

/**
 * The model for the Whack-A-Mole game
 *
 * @author primary srikamal
 * @author secondary Craig Gebo
 */
public class WAMBoard
{
    public int ROWS;
    public int COLS;
    public int Players;
    public String Score;

    /**
     * Used to keep track of the Mole status on the board
     */
    public enum Mole
    {
        MOLE_UP, MOLE_DOWN
    }

    /** the board */
    private Mole[][] board;

    /**
     * useful variables
     */

    /**
     * the observers of this model
     */
    private List<Observer<WAMBoard>> observers;

    /**
     * current game status
     */
    private Status status;

    /**
     * constructor
     */
    public WAMBoard(int rows, int cols, int players)
    {
        this.ROWS = rows;
        this.COLS = cols;
        this.board = new Mole[rows][cols];
        this.Players = players;
        this.status = Status.NOT_OVER;
        this.observers = new LinkedList<>();

        for(int col=0; col<COLS; col++)
        {
            for(int row=0; row < ROWS; row++)
            {
                board[row][col] = Mole.MOLE_DOWN;
            }
        }
    }

    /**
     * possible status of the game
     */
    public enum Status
    {
        NOT_OVER, GAME_WON, GAME_LOST, GAME_TIED, ERROR;
    }

    /**
     * The view calls this method to add themselves as an observer of the model
     */
    public void addObserver(Observer<WAMBoard> observer)
    {
        this.observers.add(observer);
    }

    /**
     * When the model changes, the observers are notified via their update methid
     */
    private void alertObservers()
    {
        for(Observer<WAMBoard> obs: this.observers)
        {
            obs.update(this);
        }
    }

    /**
     * get game status
     */
    public Status getStatus()
    {
        return this.status;
    }

    /**
     * The user may close at any time.
     */
    public void close()
    {
        //alertObservers();
    }

    /**
     * Called when the game has been won by this player.
     */
    public void gameWon()
    {
        this.status = Status.GAME_WON;
        alertObservers();
    }

    /**
     * Called when the game has been won by the other player.
     */
    public void gameLost()
    {
        this.status = Status.GAME_LOST;
        alertObservers();
    }

    /**
     * Called when the game has been tied.
     */
    public void gameTied()
    {
        this.status = Status.GAME_TIED;
        alertObservers();
    }

    /**
     * to inform them that a mole has gone down
     */
    public void moleUp(int row, int col)
    {
        this.board[row-1][col-1] = Mole.MOLE_UP;
        alertObservers();
    }

    /**
     * to inform players that a mole has gone down
     */
    public void moleDown(int row, int col)
    {
        this.board[row-1][col-1] = Mole.MOLE_DOWN;
        alertObservers();
    }

    public void Error(String arguments)
    {
        this.status = Status.ERROR;
        alertObservers();
    }

    /**
     * initializing the score
     */
    public String Score()
    {
        return this.Score;
    }

    /**
     * What is at this square?
     * @param row row number of square
     * @param col column number of square
     * @return Mole at the given location
     */
    public Mole getContents(int row, int col)
    {
        return this.board[row][col];
    }
}

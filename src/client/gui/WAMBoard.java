package client.gui;

import java.util.LinkedList;
import java.util.List;

/**
 * The model for the Whack-A-Mole game
 *
 * @author srikamal
 */
public class WAMBoard
{
    /**
     * useful variables
     */
    public int ROWS;
    public int COLS;
    public int Players;
    public String Score;

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
        this.Players = players;
        this.status = Status.NOT_OVER;
        this.observers = new LinkedList<>();
    }

    /**
     * possible status of the game
     */
    public enum Status
    {
        NOT_OVER, I_WON, I_LOST, TIE, ERROR;

        private String message = null;

        public void setMessage(String msg)
        {
            this.message = msg;
        }

        @Override
        public String toString()
        {
            super.toString();
            return '(' + this.message + ')';
        }
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
        alertObservers();
    }

    /**
     * Called when the game has been won by this player.
     */
    public void gameWon()
    {
        this.status = Status.I_WON;
        alertObservers();
    }

    /**
     * Called when the game has been won by the other player.
     */
    public void gameLost()
    {
        this.status = Status.I_LOST;
        alertObservers();
    }

    /**
     * Called when the game has been tied.
     */
    public void gameTied()
    {
        this.status = Status.TIE;
        alertObservers();
    }

    /**
     * to inform them that a mole has gone down
     */
    public void moleUp()
    {
        alertObservers();
    }

    /**
     * to inform players that a mole has gone down
     */
    public void moleDown()
    {
        alertObservers();
    }

    /**
     * to let the players send a whack message
     */
    public void Wack(int col, int row)
    {
        //hit at location board[row][col]
    }

    public void Error(String arguments)
    {
        this.status = Status.ERROR;
        this.status.setMessage(arguments);
        alertObservers();
    }

    /**
     * initializing the score
     */
    public void Score(String score)
    {
        this.Score = score;
    }
}

package server;

import static java.lang.Math.floor;

/**
 * Keeps track of which moles are up and down on the server side.\
 *
 * @author Sri Kamal
 * @author Craig Gebo
 */
public class WAM
{
    /** The number of rows */
    private final int ROWS;
    /** The number of columns */
    private final int COLS;
    /** List of all moles */
    private Mole[][] board;
    /** List of all players */
    private WAMPlayer[] players;

    /**
     * Represents whether or not a mole is currently
     * up or down.
     */
    private enum Mole
    {
        MOLE_UP, MOLE_DOWN
    }

    /**
     * Creates a new WAM.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param players list of players
     */
    public WAM(int rows, int cols, WAMPlayer[] players)
    {
        this.ROWS = rows;
        this.COLS = cols;
        this.board = new Mole[rows][cols];
        this.players = players;

        for(int col=0; col < COLS; col++)
        {
            for(int row=0; row < ROWS; row++)
            {
                board[row][col] = Mole.MOLE_DOWN;
            }
        }
    }

    /**
     * Checks whether or not a mole is up.
     *
     * @param moleID the ID of the mole
     * @return true if the mole is up, false otherwise
     */
    public boolean isMoleUp(int moleID)
    {
        int row = (int) floor((double) moleID / (double) COLS) ;
        System.out.println(row);
        int col = (moleID % COLS) ;
        System.out.println(col);
        return (board[row][col].equals(Mole.MOLE_UP));
    }

    /**
     * Gets the number of columns.
     *
     * @return the number of columns
     */
    public int getCols() {
        return this.COLS;
    }

    /**
     * Sets a mole to be up at a given position.
     *
     * @param row the row
     * @param col the column
     */
    public void moleUp(int row, int col)
    {
        this.board[row-1][col-1] = Mole.MOLE_UP;
    }

    /**
     * Sets a mole to be down at a given position.
     *
     * @param row the row
     * @param col the column
     */
    public void moleDown(int row, int col)
    {
        this.board[row-1][col-1] = Mole.MOLE_DOWN;
    }

    /**
     * Checks which player won the game.
     *
     * @return the player which won the game
     */
    public WAMPlayer hasWon()
    {
        int highscore = 0;
        for (WAMPlayer current:players)
        {
            if(current.score>highscore)
            {
                highscore = current.score;
            }
        }
        for (WAMPlayer current:players)
        {
            if(highscore == current.score)
            {
                return current;
            }
        }
        return null;
    }

    /**
     * Checks if the game was a tie.
     *
     * @return true if there was a tie, false otherwise.
     */
    public boolean hasTied()
    {
        int highscore = 0;
        for (WAMPlayer current: players)
        {
            if(highscore == current.score)
            {
                return true;
            }
            if(current.score>highscore)
            {
                highscore = current.score;
            }
        }
        return false;
    }
}

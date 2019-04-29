package server;

import static java.lang.Math.floor;

/**
 *
 */
public class WAM
{
    /**  */
    private int ROWS;
    /**  */
    private int COLS;
    /**  */
    private Mole[][] board;
    /**  */
    private WAMPlayer[] players;

    /**
     *
     */
    private enum Mole
    {
        MOLE_UP, MOLE_DOWN
    }

    /**
     *
     * @param rows
     * @param cols
     * @param players
     */
    public WAM(int rows, int cols, WAMPlayer[] players)
    {
        this.ROWS = rows;
        this.COLS = cols;
        this.board = new Mole[rows][cols];
        this.players = players;

        for(int col=0; col<COLS; col++)
        {
            for(int row=0; row < ROWS; row++)
            {
                board[row][col] = Mole.MOLE_DOWN;
            }
        }
    }

    /**
     *
     * @param moleID
     * @return
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
     *
     * @return
     */
    public int getCols() {
        return this.COLS;
    }

    /**
     *
     * @param row
     * @param col
     */
    public void moleUp(int row, int col)
    {
        this.board[row-1][col-1] = Mole.MOLE_UP;
    }

    /**
     *
     * @param row
     * @param col
     */
    public void moleDown(int row, int col)
    {
        this.board[row-1][col-1] = Mole.MOLE_DOWN;
    }

    /**
     *
     * @return
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
     *
     * @return
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

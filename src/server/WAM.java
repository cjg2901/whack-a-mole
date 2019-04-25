package server;

public class WAM
{

    /**
     * Used to indicate a mole that has been made on the board.
     */
    public enum Mole
    {
        MOLE_UP, MOLE_DOWN
    }

    public int ROWS;
    public int COLS;

    private Mole[][] board;

    private WAMPlayer[] player;

    public WAM(int rows, int cols, WAMPlayer[] player)
    {
        this.ROWS = rows;
        this.COLS = cols;
        this.player = new WAMPlayer[player.length];
        this.board = new Mole [ROWS][COLS];
        for(int col=0; col<COLS; col++)
        {
            for(int row=0; row < ROWS; row++)
            {
                board[row][col] = Mole.MOLE_DOWN;
            }
        }
        for (int i = 0; i < player.length; i ++)
        {
            this.player[i] = player[i];
        }
    }

    public WAMPlayer hasWonGame()
    {
        int highscore = 0;
        for (WAMPlayer currentWinner : this.player)
        {
            if (currentWinner.score > highscore)
            {
                highscore = currentWinner.score;

            }
        }
        for (WAMPlayer Winner : this.player)
        {
            if (Winner.score == highscore)
            {
                return Winner;
            }
        }
        return null;
    }

    public boolean hasTied()
    {
        return false;
    }
}

package server;

import client.gui.WAMBoard;

import static common.WAMProtocol.*;

public class WAM
{
    private int ROWS;
    private int COLS;
    private Mole[][] board;
    private WAMPlayer[] players;
    private WAMBoard.Status status;

    private enum Mole
    {
        MOLE_UP, MOLE_DOWN
    }

    public WAM(int rows, int cols, WAMPlayer[] players)
    {
        this.ROWS = rows;
        this.COLS = cols;
        this.board = new Mole[rows][cols];
        this.players = players;
        this.status = WAMBoard.Status.NOT_OVER;

        for(int col=0; col<COLS; col++)
        {
            for(int row=0; row < ROWS; row++)
            {
                board[row][col] = Mole.MOLE_DOWN;
            }
        }
    }

    public void moleUp(int row, int col)
    {
        this.board[row-1][col-1] = Mole.MOLE_UP;
    }

    public void moleDown(int row, int col)
    {
        this.board[row-1][col-1] = Mole.MOLE_DOWN;
    }

    public WAMPlayer hasWON()
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

//    public void WhackDetected()
//    {
//        String message = scanner.nextLine();
//        String[] messagelist = message.split(" ");
//        switch (messagelist[0])
//        {
//            case WHACK:
//                printer.println(MOLE_DOWN + messagelist[1]);
//                this.score += 2;
//            default:
//                printer.println(ERROR);
//        }
//    }
}

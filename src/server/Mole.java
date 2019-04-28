package server;
import java.util.Random;

import static java.lang.Math.floor;

/**
 * Class represents the moles which independently and randomly go up
 * and down.
 *
 * @author Craig Gebo @ cjg2901@rit.edu
 */
public class Mole extends Thread {

    /** The number of the mole in the game */
    private int moleNumber;
    /** The players of the game */
    private WAMPlayer[] players;
    /** Whether or not the game is running */
    private boolean gameRunning;

    private WAM game;

    /**
     * Creates a new mole
     *
     * @param moleNumber the number of the mole in the game
     * @param players the players of the game
     */
    public Mole(int moleNumber, WAMPlayer[] players, WAM game) {
        this.moleNumber = moleNumber;
        this.players = players;
        this.gameRunning = true;
        this.game = game;
    }

    /**
     * Sets gameRunning to false because game is over.
     */
    public void stopGame() {
        this.gameRunning = false;
    }

    /**
     * While the game is running, sleep a random amount of time,
     * send the moleUp message to the players, then sleep for 5 seconds and send
     * the moleDown message.
     */
    @Override
    public void run() {
        while (gameRunning) {
            try {
                Random random = new Random();
                sleep(random.nextInt(10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (WAMPlayer player : players) {
                player.moleUp(moleNumber);
                int row = (int) floor((double) moleNumber / (double) game.getCols()) + 1;
                int col = (moleNumber % game.getCols()) + 1;
                game.moleUp(row, col);
            }

            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (WAMPlayer player : players) {
                player.moleDown(moleNumber);
                int row = (int) floor((double) moleNumber / (double) game.getCols()) + 1;
                int col = (moleNumber % game.getCols()) + 1;
                game.moleDown(row, col);
            }
        }
    }
}

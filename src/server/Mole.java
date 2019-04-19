package server;
import java.util.Random;

public class Mole implements Runnable {

    private int moleNumber;
    private WAMPlayer[] players;

    public Mole(int moleNumber, WAMPlayer[] players) {
        this.moleNumber = moleNumber;
        this.players = players;
    }

    @Override
    public void run() {
        for (WAMPlayer player : players) {
            player.moleUp(moleNumber);
        }
    }
}

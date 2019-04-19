package server;
import java.util.Random;

public class Mole extends Thread {

    private int moleNumber;
    private WAMPlayer[] players;

    public Mole(int moleNumber, WAMPlayer[] players) {
        this.moleNumber = moleNumber;
        this.players = players;
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            sleep(random.nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (WAMPlayer player : players) {
            player.moleUp(moleNumber);
        }

        try {
            Random random = new Random();
            sleep(random.nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (WAMPlayer player : players) {
            player.moleDown(moleNumber);
        }
    }
}

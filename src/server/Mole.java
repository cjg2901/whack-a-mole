package server;
import java.util.Random;

public class Mole extends Thread {

    private int moleNumber;
    private WAMPlayer[] players;
    private boolean gameRunning;

    public Mole(int moleNumber, WAMPlayer[] players) {
        this.moleNumber = moleNumber;
        this.players = players;
        this.gameRunning = true;
    }

    public void stopGame() {
        this.gameRunning = false;
    }

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
            }

            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (WAMPlayer player : players) {
                player.moleDown(moleNumber);
            }
        }
    }
}

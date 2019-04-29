package server;

import common.WAMException;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static common.WAMProtocol.*;

/**
 * Represents a single player in the game.
 *
 * @author Craig Gebo @ cjg2901@rit.edu
 */
public class WAMPlayer implements Closeable, Runnable {
    /**
     * The {@link Socket} used to communicate with the client.
     */
    private Socket sock;

    private Scanner scanner;
    /**
     * The {@link PrintStream} used to send requests to the client.
     */
    private PrintStream printer;

    /** The number of rows in the board */
    private int rows;
    /** The number of columns in the board */
    private int cols;
    /** The total number of players in the game */
    private int numPlayers;
    /** A number to represent the player */
    private int playerNumber;
    /** The player's score */
    public int score;

    private WAMPlayer[] players;

    private WAMGame game;

    /**
     * Creates a new {@link WAMPlayer} that will use the specified
     * {@link Socket} to communicate with the client.
     *
     * @param sock The {@link Socket} used to communicate with the client.
     *
     * @throws common.WAMException If there is a problem establishing
     * communication with the client.
     */
    public WAMPlayer(Socket sock, int rows, int cols, int numPlayers, int playerNumber) throws WAMException {
        this.sock = sock;
        try {
            printer = new PrintStream(sock.getOutputStream());
        }
        catch (IOException e) {
            throw new WAMException(e);
        }
        this.rows = rows;
        this.cols = cols;
        this.numPlayers = numPlayers;
        this.playerNumber = playerNumber;
        this.score = 0;
    }

    public void setPlayers(WAMPlayer[] players) {
        this.players = players;
    }

    /**
     * Sends the initial WELCOME message to the client.
     */
    public void connect() {
        printer.println(WELCOME + " " + rows + " " + cols + " " + numPlayers + " " + playerNumber);
        printer.flush();
    }

    public void setgame(WAMGame game)
    {
        this.game = game;
    }

    /**
     * Sends a MOLE_UP message to the client with a given mole number.
     *
     * @param moleNumber the number of the mole which is up
     */
    public synchronized void moleUp(int moleNumber) {
        printer.println(MOLE_UP + " " + moleNumber);
        printer.flush();
    }

    /**
     * Sends a MOLE_DOWN message to the client with a given mole number.
     *
     * @param moleNumber the number of the mole which is down
     */
    public synchronized void moleDown(int moleNumber) {
        printer.println(MOLE_DOWN + " " + moleNumber);
        printer.flush();
    }

    /**
     * Called to send a GAME_WON request to the client because the
     * player's most recent move won the game.
     *
     */
    public void gameWon() {
        printer.println(GAME_WON);
        printer.flush();

    }

    /**
     * Called to send a GAME_LOST request to the client because the
     * other player's most recent move won the game.
     *
     */
    public void gameLost()  {
        printer.println(GAME_LOST);
        printer.flush();
    }

    /**
     * Called to send a GAME_TIED request to the client because the
     * game tied.
     */
    public void gameTied()  {
        printer.println(GAME_TIED);
        printer.flush();
    }

    //make a function to handle whack messages

    /**
     * Called to send an ERROR to the client. This is called if either
     * client has invalidated themselves with a bad response.
     *
     * @param message The error message.
     */
    public void error(String message) {
        printer.println(ERROR + " " + message);
        printer.flush();
    }

    /**
     * Called to close the client connection after the game is over.
     */
    @Override
    public void close() {
        try {
            sock.close();
        }
        catch(IOException ioe) {
        }
    }

    @Override
    public void run()
    {
        try {
            scanner = new Scanner(sock.getInputStream());
            printer = new PrintStream(sock.getOutputStream());
            while (true)
            {
                String request = this.scanner.nextLine();
                String[] tokens = request.split(" ");
                System.out.println(request);
                if (tokens[0].equals(WHACK))
                {
                    if (game.getGame().isMoleUP(Integer.parseInt(tokens[1])))
                    {
                        this.score += 2;
                        for (WAMPlayer player:players)
                        {
                            player.printer.println(MOLE_DOWN + " " + tokens[1]);
                            player.printer.flush();
                        }
                    }
                    else
                    {
                        this.score -= 1;
                    }
                    String scoreMessage = SCORE;
                    for (WAMPlayer player : players) {
                        scoreMessage += " " + player.score;
                    }
                    for (WAMPlayer player : players) {
                        player.printer.println(scoreMessage);
                        player.printer.flush();
                    }
                }
            }
        }
        catch (IOException e) {
            try {
                throw new WAMException(e);
            } catch (WAMException ex) {
                ex.printStackTrace();
            }
        } catch (NoSuchElementException nse)
        {
            //nse.printStackTrace();
        }
    }
}

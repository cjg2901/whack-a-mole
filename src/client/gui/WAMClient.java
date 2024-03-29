package client.gui;
/**
 * needs to handle GameWon etc messages
 */

import common.WAMException;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import static common.WAMProtocol.*;
import static java.lang.Math.*;

/**
 * Client which interfaces with the server for WAM.
 * Communicates server messages to the model.
 *
 * @author Craig Gebo
 * @author Sri Kamal
 */
public class WAMClient {

    /** Turn on if standard output debug messages are desired. */
    private static final boolean DEBUG = true;

    /** client socket to communicate with server */
    private Socket clientSocket;
    /** used to read requests from the server */
    private Scanner networkIn;
    /** Used to write responses to the server. */
    private PrintStream networkOut;
    /** the model which keeps track of the game */
    public WAMBoard board;

    /** The request */
    public String[] request1;

    /** The players ID */
    public int playerid;

    /** The players score */
    public int score;

    /**
     * Print method that does something only if DEBUG is true
     *
     * @param logMsg the message to log
     */
    private static void dPrint( Object logMsg )
    {
        if ( WAMClient.DEBUG )
        {
            System.out.println( logMsg );
        }
    }

    /** sentinel loop used to control the main loop */
    private boolean go = true;

    /**
     *
     * @return Whether the game is running or not
     */
    private synchronized boolean goodToGo() {
        return this.go;
    }

    /**
     * Stops the game running
     */
    private synchronized void stop() {
        this.go = false;
    }


    /**
     * Creates a new client.
     *
     * @param host name of the host
     * @param port port number
     * @param board the model for the game
     * @throws WAMException
     */
    public WAMClient (String host, int port, WAMBoard board) throws WAMException {
        try
        {
            this.clientSocket = new Socket(host, port);
            this.networkIn = new Scanner(clientSocket.getInputStream());
            this.networkOut = new PrintStream(clientSocket.getOutputStream());
            this.board = board;

            //block waiting for welcome message from server
            String request = this.networkIn.nextLine();
            request1 = request.split(" ");
            playerid = Integer.parseInt(request1[request1.length-1]);
            if(!request1[0].equals(WELCOME))
            {
                throw new WAMException("Expected WELCOME from server");
            }
            WAMClient.dPrint("Connected to server " + this.clientSocket);
        }
        catch(IOException e)
        {
            throw new WAMException(e);
        }
    }


    /**
     * Throws an error and stops the game.
     */
    public void error() {
        this.board.error();
        this.stop();
    }


    /**
     * Called from the GUI when it is ready to start receiving messages
     * from the server.
     */
    public void startListener() {
        new Thread(() -> this.run()).start();
    }

    /**
     * Called when the client received a message from the server that
     * a mole is up.
     *
     * @param arguments the number of the mole
     */
    public void moleUp( String arguments ) {

        int moleNumber = Integer.parseInt(arguments);

        int row = (int) floor((double) moleNumber / (double) board.COLS) + 1;
        int col = (moleNumber % board.COLS) + 1;

        // Update the board model.
        this.board.moleUp(row, col);
    }


    /**
     * Called when the client received a message from the server that
     * a mole is down.
     *
     * @param arguments the number of the mole
     */
    public void moleDown( String arguments ) {

        String[] fields = arguments.trim().split( " " );
        int moleNumber = Integer.parseInt(fields[0]);

        int row = (int) floor((double) moleNumber / (double) board.COLS) + 1;
        int col = (moleNumber % board.COLS) + 1;

        // Update the board model.
        this.board.moleDown(row, col);
    }


    /**
     * Closes the socket to the server.
     */
    public void close()
    {
        try {
            this.clientSocket.close();
        }
        catch( IOException ioe ) {}
    }


    /**
     * Runs the client, gets messages from the server.
     */
    private void run() {
        while (this.goodToGo()) {
            try {
                String request = this.networkIn.nextLine();
                String[] tokens = request.split(" ");
                System.out.println(request);

                switch ( tokens[0] ) {
                    case MOLE_UP:
                        this.moleUp(tokens[1]);
                        break;
                    case MOLE_DOWN:
                        this.moleDown( tokens[1] );
                        break;
                    case ERROR:
                        error();
                        break;
                    case SCORE:
                        updateScore(tokens[playerid+1]);
                        break;
                    case GAME_LOST:
                        gameLost();
                        this.go = false;
                        break;
                    case GAME_TIED:
                        gameTied();
                        this.go = false;
                        break;
                    case GAME_WON:
                        gameWon();
                        this.go = false;
                        break;
                    default:
                        System.err.println("Unrecognized request: " + request);
                        this.stop();
                        break;
                }
            } catch( Exception e ) {
                // Looks like the connection shut down.
                this.error();
                this.stop();
            }
        }

        this.close();
    }


    /**
     * Whack message sends that a mole was whacked by player
     * @param row the row
     * @param col the column
     */
    public void Whack(int row, int col)
    {
        int moleid = (((row)*board.COLS))+(col);
        this.networkOut.println(WHACK + " " + moleid + " " + this.playerid);
    }

    /**
     * updates the score on the gui
     * @param score the new score
     */
    public void updateScore(String score)
    {
        int newScore = Integer.parseInt(score);
        this.score = newScore;
    }

    /**
     * Called when the game has been won by this player.
     */
    public void gameWon()
    {
        this.board.gameWon();
    }

    /**
     * Called when the game has been won by the other player.
     */
    public void gameLost()
    {
        this.board.gameLost();
    }

    /**
     * Called when the game has been tied.
     */
    public void gameTied()
    {
        this.board.gameTied();
    }


}

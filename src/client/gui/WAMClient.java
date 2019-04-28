package client.gui;
/**
 * needs to handle GameWon etc messages
 */

import common.WAMException;

import java.io.*;
import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static common.WAMProtocol.*;
import static java.lang.Math.*;

/**
 * Client which interfaces with the server for WAM.
 * Communicates server messages to the model.
 *
 * @author Craig Gebo
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

    public String[] request1;

    public int player_id;

    public int ergebnis;

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
            player_id = Integer.parseInt(request1[request1.length-1]);
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
     * @param arguments
     */
    public void error( String arguments ) {
        this.board.Error( arguments );
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
            this.board.close();
        }
        catch( IOException ioe ) {
            // squash
        }
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
                        error( tokens[1] );
                        break;
                    case SCORE:
                        updateScore(tokens[1]);
                        break;
                    case GAME_LOST:
                        gameLost();
                        break;
                    case GAME_TIED:
                        gameTied();
                        break;
                    case GAME_WON:
                        gameWon();
                        break;
                    default:
                        System.err.println("Unrecognized request: " + request);
                        this.stop();
                        break;
                }
            }

            catch( NoSuchElementException nse ) {
                // Looks like the connection shut down.
                this.error( "Lost connection to server." );
                this.stop();
            }
            catch( Exception e ) {
                this.error( e.getMessage() + '?' );
                this.stop();
            }
        }

        this.close();
    }


    public void Whack(int fake_af_i, int fake_af_j)
    {
        System.out.println(fake_af_i);
        System.out.println(fake_af_j);
        int moleid = (((fake_af_i)*board.COLS))+(fake_af_j);
        this.networkOut.println(WHACK + " " + moleid + " " + this.player_id);
    }

    public void updateScore(String score)
    {
        int wertung = Integer.parseInt(score);
        this.ergebnis+=wertung;
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

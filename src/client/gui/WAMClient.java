package client.gui;

import common.WAMProtocol;
import common.WAMException;

import java.io.*;
import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static common.WAMProtocol.*;

/**
 *
 */
public class WAMClient {

    /** Turn on if standard output debug messages are desired. */
    private static final boolean DEBUG = false;

    /** client socket to communicate with server */
    private Socket clientSocket;
    /** used to read requests from the server */
    private Scanner networkIn;
    /** Used to write responses to the server. */
    private PrintStream networkOut;
    /** the model which keeps track of the game */
    private WAMBoard board;

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

    public String arguments;

    /** sentinel loop used to control the main loop */
    private boolean go;

    private synchronized boolean goodToGo() {
        return this.go;
    }

    private synchronized void stop() {
        this.go = false;
    }

    public WAMClient (String host, int port, WAMBoard board) throws WAMException {
        try
        {
            this.clientSocket = new Socket(host, port);
            this.networkIn = new Scanner(clientSocket.getInputStream());
            this.networkOut = new PrintStream(clientSocket.getOutputStream());
            this.board = board;

            //block waiting for welcome message from server
            String request = this.networkIn.next();
            this.arguments = this.networkIn.nextLine();
            if(!request.equals(WELCOME))
            {
                throw new WAMException("Expected CONNECT from server");
            }
            WAMClient.dPrint("Connected to server " + this.clientSocket);
        }
        catch(IOException e)
        {
            throw new WAMException(e);
        }
    }

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

    public void moleUp( String arguments ) {

        String[] fields = arguments.trim().split( " " );
        int number = Integer.parseInt(fields[0]);

        // Update the board model.
        this.board.moleUp(number);
    }

    public void moleDown( String arguments ) {

        String[] fields = arguments.trim().split( " " );
        int number = Integer.parseInt(fields[0]);

        // Update the board model.
        this.board.moleDown(number);
    }

    /**
     *
     */
    public void close() {
        try {
            this.clientSocket.close();
        }
        catch( IOException ioe ) {
            // squash
        }
        this.board.close();
    }

    /**

    public void run() {
        String request = "";
        while(true) {
            request = read();
            String[] tokens = request.split(" ");
            switch (tokens[0]) {
                case WAMProtocol.WELCOME:
                    System.out.println(request);
                    break;
                case WAMProtocol.MOLE_UP:
                    System.out.println(request);
                    break;
                case WAMProtocol.MOLE_DOWN:
                    System.out.println(request);
                    break;
            }
        }
    }
     */
    private void run() {
        while (this.goodToGo()) {
            try {
                String request = this.networkIn.next();
                String arguments = this.networkIn.nextLine().trim();

                switch ( request ) {
                    case MOLE_UP:
                        moleUp(arguments);
                        break;
                    case MOLE_DOWN:
                        moleDown( arguments );
                        break;
                    case ERROR:
                        error( arguments );
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

    public void Whack()
    {
        //handles whacking sends a whack
    }

//    public static void main(String[] args) throws IOException {
//        if (args.length != 2) {
//            System.out.println(
//                    "Usage: java ConnectFourClient hostname port");
//            System.exit(1);
//        }
//
//        String hostname = args[0];
//        int port = Integer.parseInt(args[1]);
//        WAMClient client = new WAMClient(hostname, port, this.board);
//        client.run();
//    }

}

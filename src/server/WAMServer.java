package server;

import common.WAMException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WAMServer implements Runnable{
    /**
     * The {@link ServerSocket} used to wait for incoming client connections.
     */
    private ServerSocket server;
    private int rows;
    private int cols;
    private int duration;
    private int numPlayers;

    /**
     * Creates a new {@link WAMServerOnePlayer} that listens for incoming
     * connections on the specified port.
     *
     * @param port The port on which the server should listen for incoming
     *             connections.
     * @throws common.WAMException If there is an error creating the
     *                              {@link ServerSocket}
     */
    public WAMServer(int port) throws WAMException {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            throw new WAMException(e);
        }
    }

    /**
     * Starts a new {@link WAMServerOnePlayer}. Simply creates the server and
     * calls {@link #run()} in the main thread.
     *
     * @param args Used to specify the port on which the server should listen
     *             for incoming client connections.
     * @throws WAMException If there is an error starting the server.
     */
    public static void main(String[] args) throws WAMException {

        if (args.length != 5) {
            System.out.println("Usage: java ConnectFourServer <port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        WAMServer server = new WAMServer(port);
        server.rows = Integer.parseInt(args[1]);
        server.cols = Integer.parseInt(args[2]);
        server.numPlayers = Integer.parseInt(args[3]);
        server.duration = Integer.parseInt(args[4]);
        server.run();

    }

    /**
     * Waits for two clients to connect. Creates a {@link }
     * for each and then pairs them off in a {@link }.<P>
     */
    @Override
    public void run() {
        try {
            WAMPlayer[] players = new WAMPlayer[]{};
            for(int i=0; i < numPlayers; i++) {
                System.out.println("Waiting for player " + i + "...");
                Socket playerSocket = server.accept();
                WAMPlayer player = new WAMPlayer(playerSocket, rows, cols, numPlayers, i);
                player.connect();
                System.out.println("Player " + i + " connected!");
            }
            System.out.println("Starting game!");
            WAMGame game = new WAMGame(players);
            /**
            WAMGame game;
            Socket playerOneSocket;
            WAMPlayer playerOne;
            Socket playerTwoSocket;
            WAMPlayer playerTwo;
            Socket playerThreeSocket;
            WAMPlayer playerThree;
            switch (numPlayers) {
                case 1:
                    System.out.println("Waiting for player one...");
                    playerOneSocket = server.accept();
                    playerOne =
                            new WAMPlayer(playerOneSocket, rows, cols, numPlayers, 1);
                    playerOne.connect();
                    System.out.println("Player one connected!");
                    System.out.println("Starting game!");
                    game = new WAMGame(playerOne);
                    break;
                case 2:
                    System.out.println("Waiting for player one...");
                    playerOneSocket = server.accept();
                    playerOne =
                            new WAMPlayer(playerOneSocket, rows, cols, numPlayers, 1);
                    playerOne.connect();
                    System.out.println("Player one connected!");

                    System.out.println("Waiting for player two...");
                    playerTwoSocket = server.accept();
                    playerTwo =
                            new WAMPlayer(playerTwoSocket, rows, cols, numPlayers, 2);
                    playerTwo.connect();
                    System.out.println("Player two connected!");
                    System.out.println("Starting game!");
                    game = new WAMGame(playerOne, playerTwo);
                    break;

                case 3:
                    System.out.println("Waiting for player one...");
                    playerOneSocket = server.accept();
                    playerOne =
                            new WAMPlayer(playerOneSocket, rows, cols, numPlayers, 1);
                    playerOne.connect();
                    System.out.println("Player one connected!");
                    System.out.println("Waiting for player two...");
                    playerTwoSocket = server.accept();
                    playerTwo =
                            new WAMPlayer(playerTwoSocket, rows, cols, numPlayers, 2);
                    playerTwo.connect();
                    System.out.println("Player two connected!");
                    System.out.println("Waiting for player three...");
                    playerThreeSocket = server.accept();
                    playerThree =
                            new WAMPlayer(playerThreeSocket, rows, cols, numPlayers, 3);
                    playerThree.connect();
                    System.out.println("Player three connected!");
                    System.out.println("Starting game!");
                    game = new WAMGame(playerOne, playerTwo, playerThree);
                    break;
                default:
                    game = new WAMGame();
             }
             */
            // server is not multithreaded
            new Thread(game).run();
        } catch (IOException e) {
            System.err.println("Something has gone horribly wrong!");
            e.printStackTrace();
        } catch (WAMException e) {
            System.err.println("Failed to create players!");
            e.printStackTrace();
        }
    }
}

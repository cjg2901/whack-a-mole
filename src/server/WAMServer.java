package server;

import common.WAMException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server which connects to the clients, creates all new players and the game.
 *
 * @author Craig Gebo @ cjg2901@rit.edu
 */

public class WAMServer implements Runnable{
    /**
     * The {@link ServerSocket} used to wait for incoming client connections.
     */
    private ServerSocket server;
    private int rows;
    private int cols;
    private int duration;
    private int numPlayers;
    public WAMPlayer[] players;

    /**
     * Creates a new that listens for incoming
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
     * Starts a new. Simply creates the server and
     * calls {@link #run()} in the main thread.
     *
     * @param args Used to specify the port on which the server should listen
     *             for incoming client connections.
     * @throws WAMException If there is an error starting the server.
     */
    public static void main(String[] args) throws WAMException {

        if (args.length != 5) {
            System.out.println("Usage: java WAMServer <port>");
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
     * Waits for clients to connect. Creates a new player for each connection,
     * and adds each player to a list of all players. Once all players have connected,
     * creates a new game and starts the game.
     */
    @Override
    public void run() {
        try {
            this.players = new WAMPlayer[numPlayers];
            for(int i=0; i < numPlayers; i++) {
                System.out.println("Waiting for player " + i + "...");
                Socket playerSocket = server.accept();
                WAMPlayer player = new WAMPlayer(playerSocket, rows, cols, numPlayers, i);
                players[i] = player;
                player.connect();
                System.out.println("Player " + i + " connected!");
                new Thread(player).start();
            }
            System.out.println("Starting game!");
            for (WAMPlayer player : players) {
                player.setPlayers(players);
            }
            WAMGame game = new WAMGame(rows, cols, duration, players);
            // server is not multithreaded
            new Thread(game).start();
        } catch (IOException e) {
            System.err.println("Something has gone horribly wrong!");
            e.printStackTrace();
        } catch (WAMException e) {
            System.err.println("Failed to create players!");
            e.printStackTrace();
        }
    }
}

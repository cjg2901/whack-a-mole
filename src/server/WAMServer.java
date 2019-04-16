package server;

import client.gui.WAMBoard;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static common.WAMProtocol.WELCOME;

public class WAMServer {
    /** The game of ConnectFour */
    private WAMBoard game;
    /** The socket to the first client */
    private Socket client;
    /** Input from the first client */
    private Scanner in;
    /** Output to the first client */
    private PrintWriter out;

    /**
     * Creates a new Connect Four Server
     *
     * @param c the socket to the first client
     * @param c the socket to the second client
     * @throws IOException
     */
    public WAMServer(Socket c, int rows, int cols, int numPlayers) throws IOException {
        game = new WAMBoard(rows, cols, numPlayers);
        client = c;
        in = new Scanner(c.getInputStream());
        out = new PrintWriter(c.getOutputStream());
    }

    /**
     * Sends a message to a given output
     *
     * @param message the string to be sent
     * @param out the output to send the message
     */
    public static void send(String message, PrintWriter out) {
        out.println(message);
        out.flush();
    }

    /**
     * Reads the next line from a given input
     *
     * @param in the input to read from
     * @return the next line
     */
    public String read(Scanner in) {
        return in.nextLine();
    }

    /**
     * Closes a given socket
     *
     * @param socket the socket to be closed
     * @throws IOException
     */
    public void close(Socket socket) throws IOException {
        socket.close();
    }

    public static void main(String[] args) throws IOException{
        if (args.length != 5) {
            System.out.println("Usage: java ConnectFourServer port");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        int rows = Integer.parseInt(args[1]);
        int cols = Integer.parseInt(args[2]);
        int numPlayers = Integer.parseInt(args[3]);
        int duration = Integer.parseInt(args[4]);
        ServerSocket server = new ServerSocket(port);
        System.out.println("Waiting for client...");
        Socket c = server.accept();
        String welcome = WELCOME + " " + rows + " " + cols + " " + numPlayers + " " + duration;
        send(welcome, new PrintWriter(c.getOutputStream()));
        System.out.println("Client 1 has been connected.");
        WAMServer gameServer = new WAMServer(c,rows, cols, numPlayers);
    }
}

package client.gui;

import common.WAMProtocol;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 */
public class WAMClient {
    /** The socket connected to the server */
    private Socket socket;
    /** Input from the server */
    private Scanner in;
    /** Output to the server */
    private PrintWriter out;

    public WAMClient (Socket sock) throws IOException {
        socket = sock;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    /**
     * Reads the next line from the server
     *
     * @return the next line
     */
    public String read() {
        return in.nextLine();
    }

    /**
     * Sends a message to the server
     *
     * @param message the String to be sent
     */
    public void send(String message) {
        out.println(message);
        out.flush();
    }

    /**
     * Closes the socket
     *
     * @param socket the socket to be closed
     * @throws IOException
     */
    public void close(Socket socket) throws IOException {
        socket.close();
    }

    /**
     *
     */
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

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println(
                    "Usage: java ConnectFourClient hostname port");
            System.exit(1);
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        Socket server = new Socket(hostname, port);
        WAMClient client = new WAMClient(server);
        client.run();
    }
}

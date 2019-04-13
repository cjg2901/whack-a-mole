package client.gui;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.List;

/**
 * A JavaFX GUI for the networked Whack-A-Mole Game
 * Author : Sri Kamal V. Chillarage
 */
public class WAMGUI extends Application implements Observer<WAMBoard>
{

    private WAMBoard board;
    private Image MOLE_DOWN;
    private Image MOLE_UP;
    private WAMClient client;
    private int Rows;
    private int Cols;
    private int duration;
    private int players;
    Button[][] boardarray = new Button[Rows][Cols];

    @Override
    public void init()
    {
        try
        {
            List<String> args = getParameters().getRaw();

            int port = Integer.parseInt(args.get(0));
            this.Rows = Integer.parseInt(args.get(1));
            this.Cols = Integer.parseInt(args.get(2));
            this.players = Integer.parseInt(args.get(3));
            this.duration = Integer.parseInt(args.get(4));

            this.board = new WAMBoard(this.Rows, this.Cols, this.players);
            this.board.addObserver(this);

            this.MOLE_DOWN = new Image("client.gui/WAM-logo.png");
            this.MOLE_UP = new Image("client.gui/WAM-mole.png");
            //needs some work regarding hostname
            Socket Sock = new Socket();
            //this.client = new WAMClient();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception
    {

    }

    @Override
    public void stop()
    {

    }

    private void refresh()
    {

    }

    @Override
    public void update(WAMBoard wamBoard)
    {

    }

    public static void main(String[] args)
    {

    }
}

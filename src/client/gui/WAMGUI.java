package client.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
    private Image Background;
    private WAMClient client;
    private int Rows;
    private int Cols;
    private int duration;
    private int players;
    Button[][] boardarray;

    @Override
    public void init()
    {
        try
        {
            List<String> args = getParameters().getRaw();

            String host = args.get(0);
            int port = Integer.parseInt(args.get(1));

            this.board = new WAMBoard(this.Rows, this.Cols, this.players);
            this.board.addObserver(this);

            this.MOLE_DOWN = new Image("client/gui/WAM-logo.png");
            this.MOLE_UP = new Image("client/gui/WAM-mole.png");
            this.Background = new Image("client/gui/WAM-bg.png");
            this.client = new WAMClient(host, port, this.board);

            String arguments = client.arguments;
            String[] gameinfo = arguments.split(" ");
            this.Rows = Integer.parseInt(gameinfo[1]);
            this.Cols = Integer.parseInt(gameinfo[2]);
            this.players = Integer.parseInt(gameinfo[3]);
            this.duration = Integer.parseInt(gameinfo[4]);

            this.boardarray = new Button[Rows][Cols];

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane borderpane = new BorderPane();
        GridPane pane = new GridPane();
        for(int i = 0; i < Rows; i++)
        {
            for(int j = 0; j < Cols; j++)
            {
                boardarray[i][j] = new Button();
                boardarray[i][j].setOnAction(e -> client.Whack());
                boardarray[i][j].setGraphic(new ImageView(this.MOLE_DOWN));
                pane.add(boardarray[i][j], i, j);
            }
        }
        Label header = new Label();
        header.setText("\t\t\tWelcome to Whack A Mole");
        borderpane.setTop(header);
        borderpane.setCenter(pane);

        Scene scene = new Scene(borderpane);
        stage.setScene(scene);
        stage.setTitle("ConnectFourGUI");

        stage.show();

        this.client.startListener();
    }

    @Override
    public void stop()
    {
        this.client.close();
    }

    private void refresh()
    {
        //does stuff
    }

    @Override
    public void update(WAMBoard wamBoard)
    {
        if ( Platform.isFxApplicationThread() )
        {
            this.refresh();
        }
        else
        {
            Platform.runLater(this::refresh);
        }
    }

    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("Usage: java ConnectFourGUI host port");
            System.exit(-1);
        }
        else
        {
            Application.launch(args);
        }
    }
}

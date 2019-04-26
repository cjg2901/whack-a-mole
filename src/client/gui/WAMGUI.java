package client.gui;

/**
 * handle scores,
 * send mole down/whack to server
 * sending messages to players
 */

import common.WAMException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * A JavaFX GUI for the networked Whack-A-Mole Game
 * @author Sri Kamal
 */
public class WAMGUI extends Application implements Observer<WAMBoard>
{

    /**
     * Contains all variables needed to run the GUI
     */
    private WAMBoard board;
    private Image MOLE_DOWN;
    private Image MOLE_UP;
    private Image Background;
    private WAMClient client;
    Label gamestatus = new Label();
    VBox boxyMcboxface = new VBox();
    private int Rows;
    private int Cols;
    private int duration;
    private int players;
    Button[][] boardarray;
    GridPane pane;

    /**
     * Initialises said variables and utilizes them effectively
     */
    @Override
    public void init()
    {
        try
        {
            List<String> args = getParameters().getRaw();

            String host = args.get(0);
            int port = Integer.parseInt(args.get(1));

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

            this.board = new WAMBoard(this.Rows, this.Cols, this.players);
            this.client.board = this.board;
            this.board.addObserver(this);


            this.boardarray = new Button[Cols][Rows];

        }
        catch (WAMException wame)
        {
            wame.printStackTrace();
        }
    }

    /**
     * creates the initial GUI that the USER sees.
     * @param stage
     * @throws WAMException
     */
    @Override
    public void start(Stage stage) throws WAMException
    {
        BorderPane borderpane = new BorderPane();
        pane = new GridPane();
        for(int i = 0; i < Cols; i++)
        {
            for(int j = 0; j < Rows; j++)
            {
                int fake_af_i = i;
                int fake_af_j = j;
                boardarray[i][j] = new Button();
                boardarray[i][j].setOnAction(e -> client.Whack(fake_af_i,fake_af_j));
                boardarray[i][j].setGraphic(new ImageView(this.MOLE_DOWN));
                pane.add(boardarray[i][j], i, j);
            }
        }
        Label header = new Label();
        header.setText("\t\t\t\t\t  Welcome to Whack A Mole");
        borderpane.setTop(header);
        borderpane.setCenter(pane);
        ImageView back = (new ImageView(this.Background));
        boxyMcboxface.getChildren().addAll(gamestatus);
        borderpane.setBottom(boxyMcboxface);

        Scene scene = new Scene(borderpane);
        stage.setScene(scene);
        stage.setTitle("ConnectFourGUI");

        stage.show();

        this.client.startListener();
    }

    /**
     * severs client connection
     */
    @Override
    public void stop()
    {
        this.client.close();
    }

    /**
     * Helps keep updating the GUI as per board changes
     */
    private void refresh()
    {
        WAMBoard.Status status = board.getStatus();
        switch (status)
        {
            case ERROR:
                this.gamestatus.setText( status.toString() );
                break;
            case I_WON:
                this.gamestatus.setText( "You won. Yay!" );
                break;
            case I_LOST:
                this.gamestatus.setText( "You lost. Boo!" );
                break;
            case TIE:
                this.gamestatus.setText( "Tie game. Meh." );
                break;
            default:
                this.gamestatus.setText(" ");
        }
        help_refresh();
    }

    /**
     * helper function used to update the view
     */
    private void help_refresh()
    {
        for(int i = 0; i < Cols; i++)
        {
            for(int j = 0; j < Rows; j++)
            {
                WAMBoard.Mole mole = board.getContents(i,j);
                if(mole == WAMBoard.Mole.MOLE_UP)
                {
                    ImageView pic1 = new ImageView(this.MOLE_UP);
                    this.boardarray[i][j].setGraphic(pic1);
                }
                else
                {
                    ImageView pic2 = new ImageView(this.MOLE_DOWN);
                    this.boardarray[i][j].setGraphic(pic2);
                }
            }
        }
    }

    /**
     * update method that calls refresh and ensures only Javafx threads run
     * @param wamBoard
     */
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

    /**
     * Main method
     * @param args
     */
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

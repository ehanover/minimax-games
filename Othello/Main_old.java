
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class Main_old extends Application {

    public static final int CA_SIZE = 500;
    public static final int FP_SIZE = 150;
    private Board board;
    public boolean playing = false;

    private int lineSpace = CA_SIZE/Board.SIZE;
    private int circleSize = 50;
    private int circleSpace = (lineSpace-circleSize)/2;

    private int[] lastClick = null;
    private final int depthDefault = 1;
    private int depth = depthDefault;



    @Override
    public void start(Stage stage){

        FlowPane fp = new FlowPane();
        //fp.setVgap(5);
        fp.setHgap(10);
        fp.setPrefWrapLength(CA_SIZE+FP_SIZE);

        board = null;
        //drawGrid();
        //System.out.println(board);

        //circleSize = 51;
        //lineSpace = CA_SIZE/Board.SIZE;
        //circleSpace = (lineSpace-circleSize)/2;

        Canvas canvas = new Canvas(CA_SIZE, CA_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawCanvas(gc);


        VBox vbox = new VBox(8);
        //vbox.setSpacing();
        Button b1 = new Button("New Game");
        b1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b1.setOnAction((event) -> {
            System.out.println("Staring new game...");
            board = new Board();
            playing = true;
            drawCanvas(gc);
            //setDepth();
            doTurns();
        });

        Button bq = new Button("Quit Program");
        bq.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        bq.setOnAction((event) -> {
            Platform.exit();
            System.exit(0);
        });

        //Text t = new Text(); // must update display to use this
        //t.setText("Minimax Depth: " + depth);


        fp.setOnMouseClicked(new EventHandler<MouseEvent>() { // canvas clicks
            @Override
            public void handle(MouseEvent event) {
                //System.out.println(event.getSceneX() + ", " + event.getSceneY());
                double x = event.getSceneX();
                double y = event.getSceneY();

                if(!playing) // ignore clicks on game grid when game is not playing
                    return;

                if(x > 0 && x < CA_SIZE && y > 0 && y < CA_SIZE){
                    int nx = (int)x/lineSpace;
                    int ny = (int)y/lineSpace;
                    if(board.spotValid(nx, ny, Board.TILE_HUMAN))
                        lastClick = new int[]{nx, ny};
                }
                doTurns();
                drawCanvas(gc);
            }
        });

        vbox.getChildren().addAll(b1, bq);
        fp.getChildren().addAll(canvas, vbox);

        Scene scene = new Scene(fp); //Creating a scene object
        stage.setTitle("Othello Minimax"); //Setting title to the Stage
        stage.setScene(scene); //Adding scene to the stage
        stage.show(); //Displaying the contents of the stage

    }

    private void setDepth(){
        TextInputDialog dialog = new TextInputDialog("depthDefault");
        dialog.setTitle("Input");
        dialog.setHeaderText("Enter the depth for the minimax algorithm");
        //System.out.println( Integer.parseInt(dialog.showAndWait().get()) );
        try{
            depth = Integer.parseInt(dialog.showAndWait().get());
        } catch (Exception e){
            depth = depthDefault;
        }
    }

    private void doTurns(){
        if (board.currentTurn == Board.TILE_COM) {
            System.out.print("starting computer's turn...");
            int[] m = minimax(depth, Board.TILE_COM);
            System.out.println("score " + m[2]);

            board.setSpot(m[0], m[1], Board.TILE_COM);

            board.currentTurn = Board.TILE_HUMAN;
        } else if(board.currentTurn == Board.TILE_HUMAN) {

            if(lastClick == null)
                return;

            System.out.println("starting player's turn...");


            board.setSpot(lastClick[0], lastClick[1], Board.TILE_HUMAN);
            lastClick = null;
            //System.out.println("got a click in a good spot");

            board.currentTurn = Board.TILE_COM;
            doTurns();

        }

    }

    private void drawCanvas(GraphicsContext gc){ // TODO: add a board to the function parameters?
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, CA_SIZE, CA_SIZE);

        drawLines(gc);

        if(!playing || board == null)
            return;

        for(int i=0; i<Board.SIZE; i++){
            for(int j=0; j< Board.SIZE; j++){

                int spot = board.getSpot(i, j);
                if(spot == Board.TILE_COM){
                    gc.setFill(Color.WHITE);
                    gc.fillOval((i*lineSpace)+circleSpace, (j*lineSpace)+circleSpace, circleSize, circleSize);
                    //gc.fillOval((i*lineSpace), (j*lineSpace), circleSize, circleSize);
                } else if(spot == Board.TILE_HUMAN){
                    gc.setFill(Color.BLACK);
                    gc.fillOval((i*lineSpace)+circleSpace, (j*lineSpace)+circleSpace, circleSize, circleSize);
                }
            }
        }


    }
    private void drawLines(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.setLineWidth(3);
        for(int i=0; i<Board.SIZE; i++){
            gc.strokeLine(lineSpace*i, 0, lineSpace*i, CA_SIZE);
            gc.strokeLine(0, lineSpace*i, CA_SIZE, lineSpace*i);
            
        }
    }

    // Recursive minimax at level of depth for either maximizing or minimizing player.
    // Return int[3] of {row, col, score}
    private int[] minimax(int depth, int player) {
        // Generate possible next moves in a List of int[2] of {row, col}.
        List<int[]> nextMoves = board.getPossibleMoves();

        // mySeed is maximizing while oppSeed is minimizing
        int bestScore = (player == 0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            bestScore = board.score(); // Gameover or depth reached, evaluate score
        } else {
            for (int[] move : nextMoves) {

                int[][] oldBoard = new int[Board.SIZE][Board.SIZE];
                for(int i=0; i<Board.SIZE; i++){
                    for(int j=0; j<Board.SIZE; j++){
                        oldBoard[i][j] = board.getBoard()[i][j];
                    }
                }
                // Try this move for the current "player"
                board.setSpot(move[0], move[1], player);
                if (player == Board.TILE_COM) {  // mySeed (computer) is maximizing player
                    currentScore = minimax(depth - 1, 1)[2];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else if(player == 1) {  // oppSeed is minimizing player (ARE THESE RIGHT NUMBERS? THIS WAS "1" BEFORE)
                    currentScore = minimax(depth - 1, 0)[2];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                // Undo move
                //board.setSpot(move, -1);
                //board.undoMove();
                for(int i=0; i<Board.SIZE; i++){
                    for(int j=0; j<Board.SIZE; j++){
                        board.setSpot(i, j, oldBoard[i][j]);
                    }
                }
            }
        }
        //System.out.println("best score in minimax: " + bestScore);
        return new int[] {bestRow, bestCol, bestScore};
    }

    public static void main(String[] args) {
        launch(args);
    }
}

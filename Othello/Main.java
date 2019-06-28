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
import java.util.concurrent.TimeUnit;

/*
TODO:
-minimax seems to be working
-there's no rules about where you can move. Moves must cause other tiles to flip
-some moves cause updates in incorrect spots (through other tiles?)
-"new game" button does not work while the doComTurn thread is running
-the minimax depth can only be changed through the code
-no aplha-beta pruning, but it may not be worth it. The minimax is slow right now, but that's probably because there are no move rules
 */

public class Main extends Application {

    public static final int CA_SIZE = 500;
    public static final int FP_SIZE = 150;
    private Board board;
    public boolean playing = false;

    private int lineSpace = CA_SIZE/Board.SIZE;
    private int circleSize = 50;
    private int circleSpace = (lineSpace-circleSize)/2;

    private int[] lastClick = null;
    private final int depthDefault = 3;
    private int depth = depthDefault;

    GraphicsContext gc;


    @Override
    public void start(Stage stage){

        FlowPane fp = new FlowPane();
        //fp.setVgap(5);
        fp.setHgap(10);
        fp.setPrefWrapLength(CA_SIZE+FP_SIZE);

        Canvas canvas = new Canvas(CA_SIZE, CA_SIZE);
        gc = canvas.getGraphicsContext2D();
        drawCanvas(gc);

        VBox vbox = new VBox(8);
        Button b1 = new Button("New Game");
        b1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b1.setOnAction((event) -> {
            System.out.println("Staring new game...");
            board = new Board();
            playing = true;
            drawCanvas(gc);
            //setDepth();
            //doTurns();
        });

        Button bq = new Button("Quit Program");
        bq.setOnAction((event) -> {
            Platform.exit();
            System.exit(0);
        });

        //Text t = new Text(); // must update display to use this
        //t.setText("Minimax Depth: " + depth);

        board = null;

        fp.setOnMouseClicked(new EventHandler<MouseEvent>() { // canvas clicks
            @Override
            public void handle(MouseEvent event) {

                if(!playing) // ignore clicks on game grid when game is not playing
                    return;

                double x = event.getSceneX();
                double y = event.getSceneY();

                if(x > 0 && x < CA_SIZE && y > 0 && y < CA_SIZE){
                    int nx = (int)(x/lineSpace);
                    int ny = (int)(y/lineSpace);
                    if(board.spotValid(nx, ny, Board.TILE_HUMAN)) {
                        if(board.isGameOver()){
                            playing = false;
                        }

                        lastClick = new int[]{nx, ny};
                        doHumanTurn();

                        //thread.start();
                        //drawCanvas(gc);

                        //doComTurn();
                        new Thread(new Runnable() { // this is supposed to allow the drawing to happen before minimax starts
                            @Override
                            public void run() {
                                doComTurn();
                            }
                        }).start();

                    }
                }

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

//    private void doTurns(){
//        //Thread.sleep(5);
////        try {
////            TimeUnit.SECONDS.sleep(5);
////        } catch(Exception e){
////            System.out.println(e);
////        }
//        if (board.currentTurn == Board.TILE_COM) {
//
//        } else if(board.currentTurn == Board.TILE_HUMAN) {
//
//        }
//
//    }

    private void doComTurn(){
        System.out.print("computer's turn starting...");

        int[] m = minimax(depth, Board.TILE_COM);
        System.out.println("score " + m[2]);
        board.setSpot(m[0], m[1], Board.TILE_COM);
        board.currentTurn = Board.TILE_HUMAN;

        drawCanvas(gc);
        System.out.println("computer's turn is finished.");
    }
    private void doHumanTurn(){
        if(lastClick == null)
            return;

        System.out.println("player's turn starting...");

        board.setSpot(lastClick[0], lastClick[1], Board.TILE_HUMAN);
        board.currentTurn = Board.TILE_COM;
        lastClick = null;

        drawCanvas(gc);
        System.out.println("player's turn is finished.");
    }

    private void drawCanvas(GraphicsContext gc){ // TODO: add a board to the function parameters?
        System.out.println("drawing canvas");
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
    // comments about seeds are probably incorrect.
    private int[] minimax(int depth, int player) {
        // Generate possible next moves in a List of int[2] of {row, col}.
        List<int[]> nextMoves = board.getPossibleMoves();

        // mySeed is maximizing while oppSeed is minimizing
        int bestScore = (player == Board.TILE_COM) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
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
                } else if (player == Board.TILE_HUMAN) {  // oppSeed is minimizing player (ARE THESE RIGHT NUMBERS? THIS WAS "1" BEFORE)
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
                board.clear();
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

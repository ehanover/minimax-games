import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    Board board;
    Scanner sc;
    Random r;

    public static int DEPTH = -1;

    public static void main(String[] args) {
        new Main();
    }

    public Main(){

        board = new Board();
        sc = new Scanner(System.in);
        r = new Random();

        System.out.println("Input a search depth for the minimax algorithm (recommended is 5-7)");
        while(DEPTH < 1 || DEPTH > 15){
            try {
                DEPTH = sc.nextInt();
            } catch (Exception e){
                System.out.println("Invalid input. Try again.");
                DEPTH = -1;
            }
        }

//        board.setSpot(0, 1);
//        board.setSpot(1, 0);
//        board.setSpot(1, 1);
//        board.setSpot(2, 0);
//        board.setSpot(2, 0);
//        board.setSpot(2, 1);
//        board.setSpot(3, 0);
//        board.setSpot(3, 0);
//        board.setSpot(3, 0);

//        board.setSpot(0, 1);
//        board.setSpot(0, 0);
//        board.setSpot(0, 0);
//        board.setSpot(0, 1);
//        board.setSpot(1, 0);
//        board.setSpot(1, 0);
//        board.setSpot(1, 1);
//        board.setSpot(2, 0);
//        board.setSpot(2, 1);
//        board.setSpot(3, 0);

//        board.setSpot(0, 1);
//        board.setSpot(1, 1);
//        board.setSpot(2, 1);
        System.out.println(board);


        while(! board.isGameOver()) {

            if(board.currentTurn == 1){ // player's turn
                int spot = -1;
                while(spot == -1 || board.spotValid(spot) == false) {
                    spot = getInput(sc);
                }
                board.setSpot(spot, 1);

//                System.out.println("board score: " + board.score());
//                System.out.println("double count: " + board.rowSame(2, 1));

//                System.out.println("available moves: ");
//                for(int i : board.getPossibleMoves()){
//                    System.out.println("    move: " + i);
//                }
                board.currentTurn = 0;

            } else if(board.currentTurn == 0){ // computer's turn

                System.out.print("Starting computer's turn... ");
                int[] m = minimax(DEPTH, 0);
                System.out.println("column " + (m[0]+1) + ", score " + m[1]);
                board.setSpot(m[0], 0);

//                int x = r.nextInt(board.SIZEX);
//                board.setSpot(x, 0);

//                int spot = -1;
//                while(spot == -1 || board.spotValid(spot) == false) {
//                    spot = getInput(sc);
//                }
//                board.setSpot(spot, 0);

                board.currentTurn = 1;
            }


            System.out.println(board);
            //System.out.println(board.score());

        }
        System.out.println("Game is over. Goodbye.");


    }

    private static int getInput(Scanner sc){
        System.out.println("Enter your move: (1-7)");
        int x = sc.nextInt()-1;
        return x;
    }


    // Recursive minimax at level of depth for either maximizing or minimizing player. Stolen from https://www.ntu.edu.sg/home/ehchua/programming/java/JavaGame_TicTacToe_AI.html
    // Return int[] of {col, score}  */
    private int[] minimax(int depth, int player) {
        // Generate possible next moves in a List of int[2] of {row, col}.
        List<Integer> nextMoves = board.getPossibleMoves();

        // mySeed is maximizing while oppSeed is minimizing
        int bestScore = (player == 0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestCol = 4; // was -1

        if (nextMoves.isEmpty() || depth == 0) {
            bestScore = board.score(); // Gameover or depth reached, evaluate score
        } else {
            for (int move : nextMoves) {
                // Try this move for the current "player"
                board.setSpot(move, player);
                if (player == 0) {  // mySeed (computer) is maximizing player
                    currentScore = minimax(depth - 1, 1)[1];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestCol = move;
                    }
                } else if(player == 1) {  // oppSeed is minimizing player
                    currentScore = minimax(depth - 1, 0)[1];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestCol = move;
                    }
                }
                // Undo move
                board.undoSpot(move);
            }
        }
        //System.out.println("best score in minimax: " + bestScore);
        return new int[] {bestCol, bestScore};
    }



}
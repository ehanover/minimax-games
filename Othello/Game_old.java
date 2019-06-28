import java.util.List;
import java.util.Scanner;
/*
public class Game {

    Board board;
    Scanner sc;

//    public static void main(String[] args) {
//        new Main();
//    }

    public Game(){

        board = new Board();
        sc = new Scanner(System.in);
        System.out.println(board);

        while(board.isGameOver() == false){


            if(board.currentTurn == 1){ // player's turn

                int[] spot = null;
                while(spot == null || board.spotEmpty(spot) == false) {
                    spot = getInput(sc);
                }
                board.setSpot(spot, 1);

                //System.out.println("board score: " + board.score());
                //System.out.println("double count: " + board.countDoubles(1));
//                System.out.println("available moves: ");
//                for(int[] i : board.getPossibleMoves()){
//                    System.out.println("    move: " + i[0] + ", " + i[1]);
//                }

                board.currentTurn = 0;

            } else if(board.currentTurn == 0){ // computer's turn

                System.out.println("Starting computer's turn...");
                int[] m = minimax(5, 0);
                //System.out.println("minimax results: " + m[0] + ", " + m[1]);
                board.setSpot(new int[]{m[0], m[1]}, 0);

                board.currentTurn = 1;
            }



            System.out.println(board);
        }
        System.out.println("goodbye.");


    }

    private static int[] getInput(Scanner sc){
        System.out.println("Enter your move: ");
        int x = sc.nextInt();
        int y = sc.nextInt();
        return new int[]{y, x};
    }

*/

    /** Recursive minimax at level of depth for either maximizing or minimizing player.
     Return int[3] of {score, row, col}  */      /*
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
                // Try this move for the current "player"
                board.setSpot(move, player);
                if (player == 0) {  // mySeed (computer) is maximizing player
                    currentScore = minimax(depth - 1, 1)[2];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else if(player == 1) {  // oppSeed is minimizing player
                    currentScore = minimax(depth - 1, 0)[2];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                // Undo move
                board.setSpot(move, -1);
            }
        }
        //System.out.println("best score in minimax: " + bestScore);
        return new int[] {bestRow, bestCol, bestScore};
    }



}
*/

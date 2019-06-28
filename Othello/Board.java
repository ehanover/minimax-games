import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int SIZE = 8;
    public static final int TILE_EMPTY = -1;
    public static final int TILE_COM = 0;
    public static final int TILE_HUMAN = 1; //TODO: look at rules. change names to tile_p1 and tile_p2 "the dark player moves first"

    public int currentTurn = TILE_HUMAN; // computer is 0

    public int[][] board;

    public Board() {
        board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = TILE_EMPTY;
            }
        }
        board[3][3] = TILE_HUMAN;
        board[4][4] = TILE_HUMAN;
        board[3][4] = TILE_COM;
        board[4][3] = TILE_COM;

    }

    public String toString() {
        String s = "";
        for (int[] i : board) {
            for (int j : i) {
                if (j == TILE_EMPTY) {
                    s += "- ";
                } else if (j == TILE_COM) {
                    s += "O ";
                } else if (j == TILE_HUMAN) {
                    s += "X ";
                }

            }
            s += "\n";
        }
        return s;
    }

    public boolean isGameOver() {
        if(isFull())
            return true;

        boolean comTiles = true; // are there none of this tile type?
        boolean humanTiles = true;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int t = board[i][j];
                if(t == TILE_COM) {
                    comTiles = false;
                } else if(t == TILE_HUMAN){
                    humanTiles = false;
                }

            }
        }
        return(comTiles || humanTiles);
    }

    public boolean isFull() {
        for (int[] i : board) {
            for (int j : i) {
                if (j == TILE_EMPTY)
                    return false;
            }
        }
        return true;
    }


    public boolean spotValid(int x, int y, int p) {
        if (!spotEmpty(x, y))
            return false;
        return true;
    }
    public boolean spotEmpty(int x, int y) {
        if (coordsValid(x, y)) {
            if (board[x][y] == TILE_EMPTY) {
                return true;
            }
        }
        return false;
    }
    public boolean coordsValid(int x, int y) {
        return (x < SIZE && x >= 0 && y < SIZE && y >= 0);
    }

    public void setSpot(int x, int y, int p) {

        if (spotEmpty(x, y)) { // this is probably not needed but safe
            board[x][y] = p;

            for (int i = x + 1; i < SIZE; i++) {
                if (board[i][y] == TILE_EMPTY)
                    break;
                if (board[i][y] == p) {
                    //System.out.println("found x+ row");
                    for (int s = x; s < i; s++) {
                        board[s][y] = p;
                    }
                    break;
                }
            }
            for (int i = x - 1; i > 0; i--) {
                if (board[i][y] == TILE_EMPTY)
                    break;
                if (board[i][y] == p) {
                    //System.out.println("found x- row");
                    for (int s = x; s > i; s--) {
                        board[s][y] = p;
                    }
                    break;
                }
            }
            for (int i = y + 1; i < SIZE; i++) {
                if (board[x][i] == TILE_EMPTY)
                    break;
                if (board[x][i] == p) {
                    //System.out.println("found y+ row");
                    for (int s = y; s < i; s++) {
                        board[x][s] = p;
                    }
                    break;
                }
            }
            for (int i = y - 1; i > 0; i--) {
                if (board[x][i] == TILE_EMPTY)
                    break;
                if (board[x][i] == p) {
                    //System.out.println("found y- row");
                    for (int s = y; s > i; s--) {
                        board[x][s] = p;
                    }
                    break;
                }
            }
            for (int i = 1; i < SIZE; i++) {
                if (x + i >= SIZE || y + i >= SIZE)
                    break;
                if (board[x + i][y + i] == TILE_EMPTY)
                    break;
                if (board[x + i][y + i] == p) {
                    //System.out.println("found x+ y+ row");
                    for (int s = 0; s < i; s++) {
                        board[x + s][y + s] = p;
                    }
                    break;
                }
            }
            for (int i = 1; i < SIZE; i++) {
                if (x + i >= SIZE || y - i < 0)
                    break;
                if (board[x + i][y - i] == TILE_EMPTY)
                    break;
                if (board[x + i][y - i] == p) {
                    //System.out.println("found x+ y- row");
                    for (int s = 0; s < i; s++) {
                        board[x + s][y - s] = p;
                    }
                    break;
                }
            }
            for (int i = 1; i < SIZE; i++) {
                if (x - i < 0 || y + i >= SIZE)
                    break;
                if (board[x - i][y + i] == TILE_EMPTY)
                    break;
                if (board[x - i][y + i] == p) {
                    //System.out.println("found x- y+ row");
                    for (int s = 0; s < i; s++) {
                        board[x - s][y + s] = p;
                    }
                    break;
                }
            }
            for (int i = 1; i < SIZE; i++) {
                if (x - i < 0 || y - i < 0)
                    break;
                if (board[x - i][y - i] == TILE_EMPTY)
                    break;
                if (board[x - i][y - i] == p) {
                    //System.out.println("found x- y- row");
                    for (int s = 0; s < i; s++) {
                        board[x - s][y - s] = p;
                    }
                    break;
                }
            }


        } else {
            System.out.println("error: setSpot tried to set an invalid spot: x:" + x + " y:" + y + " p:" + p);
        }
    }
    public int getSpot(int x, int y) {
        return board[x][y];
    }

    public List<int[]> getPossibleMoves() { // TODO: only allow moves that change other tiles
        List<int[]> moves = new ArrayList<int[]>();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] == TILE_EMPTY) {
                    moves.add(new int[]{i, j}); // this move is in array form, unlike the methods in this class
                }
            }
        }
        return moves;
    }

    public int[][] getBoard() {
        return board;
    }

    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = TILE_EMPTY;
            }
        }
    }


    public int score(){
        int s = 0;

        for(int[] i : board){
            for(int j : i){
                if(j == TILE_COM){
                    s += 1;
                } else if(j == TILE_HUMAN){
                    s += -1;
                }
            }
        }

        return s;

    }


}

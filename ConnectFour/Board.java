import java.util.ArrayList;
import java.util.List;

public class Board {

    public final int SIZEX = 7;
    public final int SIZEY = 6;

    public int currentTurn = 1; // computer is 0
    public int[][] board;

    public Board(){
        board = new int[SIZEX][SIZEY];

        for(int i=0; i<SIZEY; i++){
            for(int j=0; j<SIZEX; j++){
                board[j][i] = -1;
            }
        }

    }
    public String toString(){
        String s = "";
        for(int y=0; y<SIZEY; y++){
            for(int x=0; x<SIZEX; x++){

                int spot = board[x][SIZEY-y-1];

                if(spot == -1){
                    s += "| ";
                } else if(spot == 0){
                    s += "O ";
                } else if(spot == 1){
                    s += "X ";
                }

            }
            s += "\n";
        }
        return s;
    }

    public boolean isGameOver(){
        return (rowSame(0, 4) > 0 || rowSame(1, 4) > 0 || isFull());
    }

    private int countInRow(int x){ // returns the count of "open" spots in a row.
        int count = 0;
        for(int i=0; i<SIZEY; i++)
            if(board[x][i] != -1)
                count++;

        return count;
    }
    private int rowSame(int p, int c){ // counts the number of same-color rows of given length c for given player p
        int count = 0;
        for(int i=0; i<SIZEX; i++){
            for(int j=0; j<SIZEY; j++){
                //System.out.println("checking board[" + i + "+" + a + "][" + j + "]");

                if(i <= SIZEX-c) { // horizontals
                    boolean same = true;
                    for (int a = 0; a < c; a++) {
                        if (board[i + a][j] != p) {
                            same = false;
                            break; // which for does this break out of?
                        }
                    }
                    if(same)
                        count++;
                }

                if(j <= SIZEY-c) { // verticals
                    boolean same = true;
                    for (int a = 0; a < c; a++)
                        if(board[i][j+a] != p){
                            same = false;
                            break; //
                        }
                    if(same)
                        count++;
                }

                if(i <= SIZEX-c && j <= SIZEY-c) { // positive diagonals
                    boolean same = true;
                    for (int a = 0; a < c; a++)
                        if(board[i+a][j+a] != p){
                            same = false;
                            break;
                        }
                    if(same)
                        count++;
                }

                if(i < c && j > c-2){ // negative diagonals
                    boolean same = true;
                    for (int a = 0; a < c; a++)
                        if (board[i + a][j - a] != p) {
                            same = false;
                            break;
                        }
                    if (same)
                        count++;
                }


            } // j
        } // i

        return count;
    }

    public boolean isEmpty(){
        for(int[] i : board){
            for(int j : i){
                if(j != -1)
                    return false;
            }
        }
        return true;
    }
    public boolean isFull(){
        for(int[] i : board){
            for(int j : i){
                if(j == -1)
                    return false;
            }
        }
        return true;
    }

    public boolean coordsValid(int x){
        return (x < SIZEX && x >= 0);
    }
    public boolean spotValid(int x){
        if(coordsValid(x)) {
            if(countInRow(x) < SIZEY) {
                return true;
            }
        }
        return false;
    }

    public void setSpot(int x, int p){
        if(spotValid(x)){ // this is probably not needed TODO: remove this after it works
            board[x][countInRow(x)] = p;
        } else {
            System.out.println("error: setSpot tried to set an invalid spot.");
        }
    }
    public int getSpot(int x){
        int y = countInRow(x);
        if(y > 0)
            y--;
        return board[x][y];
    }
    public void undoSpot(int x){
        int y = countInRow(x);
        if(y > 0)
            y--;
        board[x][y] = -1;
    }

    public List<Integer> getPossibleMoves(){
        List<Integer> moves = new ArrayList<Integer>();
        for(int x=0; x<SIZEX; x++)
            if(spotValid(x)) // this check is extra complicated. It knows the coordinates are valid, that check could be removed
                moves.add(x);

        return moves;
    }

    public int score(){ // scores the board (positive score value for computer)
        int s = 0;

        if(isEmpty()) return 0;
//        if(isFull()){
//            System.out.println("error: score function received a full board. Returning 0 score");
//            return 0;
//        }

        s += rowSame(0, 4)*100000;
        s += rowSame(1, 4)*-100000;

        s += rowSame(0, 3)*200;
        s += rowSame(1, 3)*-200;

        s += rowSame(0, 2)*10;
        s += rowSame(1, 2)*-10;

        for(int[] i : board){
            for(int j : i){
                if(j == 0){
                    s += 1;
                } else if(j == 1){
                    s += -1;
                }
            }
        }

        return s;

    }



}
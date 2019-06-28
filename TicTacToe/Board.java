import java.util.ArrayList;
import java.util.List;

public class Board {

    public final int SIZE = 3;

    public int currentTurn = 1; // computer is 0

    public int[][] board;

    public Board(){
         board = new int[][] {
                 {-1, -1, -1},
                 {-1, -1, -1},
                 {-1, -1, -1} };

    }
    public String toString(){
        String s = "";
        for(int[] i : board){
            for(int j : i){
                if(j == -1){
                    s += "- ";
                } else if(j == 0){
                    s += "O ";
                } else if(j == 1){
                    s += "X ";
                }

            }
            s += "\n";
        }
        return s;
    }
    private int[] ia(int a, int b){
        return new int[]{a, b};
    }


    public boolean isGameOver(){
        if(has0Won() || has1Won() || isFull()){
            return true;
        }
        return false;
    }
    public boolean has0Won(){
        return winSame(0);
    }
    public boolean has1Won(){
        return winSame(1);
    }
    private boolean winSame(int t){
        for(int i=0; i<SIZE; i++){ // does not properly use SIZE variable
            if(board[0][i] == t && board[1][i] == t && board[2][i] == t)
                return true;
            if(board[i][0] == t && board[i][1] == t && board[i][2] == t)
                return true;
        }

        if(board[0][0] == t && board[1][1] == t && board[2][2] == t)
            return true;
        if(board[2][0] == t && board[1][1] == t && board[0][2] == t)
            return true;

        return false;
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

    public boolean coordsValid(int[] s){
        return (s[0] < SIZE && s[0] >= 0 && s[1] < SIZE && s[1] >= 0);
    }
    public boolean spotEmpty(int[] s){
        if(coordsValid(s)) {
            if(board[s[0]][s[1]] == -1) {
                return true;
            }
        }
        return false;
    }
    public void setSpot(int[] s, int p){
        if(coordsValid(s)){ // this is probably not needed TODO: remove this after it works
            board[s[0]][s[1]] = p;
        } else {
            System.out.println("error: setSpot tried to set an invalid spot.");
        }
    }
    public int getSpot(int[] s){
        return board[s[0]][s[1]];
    }

    public List<int[]> getPossibleMoves(){
        List<int[]> moves = new ArrayList<int[]>();
        for(int i=0; i<SIZE; i++){
            for(int j=0; j<SIZE; j++){

                if(board[i][j] == -1){
                    moves.add( new int[]{i, j} );
                }
            }
        }
        return moves;
    }


    public int score(){
        int s = 0;

        if(isEmpty()) return 0;
//        if(isFull()){
//            System.out.println("error: score function received a full board. Returning 0 score");
//            return 0;
//        }


        if(has0Won()) s += 1000;
        if(has1Won()) s += -1000;

        s += countDoubles(0)*10;
        s += countDoubles(1)*-10;

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
    private int countDoubles(int p){
        int c = 0;
        for(int i=0; i<SIZE; i++){
            for(int j=0; j<SIZE; j++){
                //System.out.println("i: " + i + ", j: " + j);

                if(getSpot(ia(i, j)) != p)
                    continue;

                for(int q=i-1; q<=i+1; q++){
                    for(int r=j-1; r<=j+1; r++){
                        if(!coordsValid(ia(q, r)) )
                            continue;
                        if(q == i && r == j)
                            continue;

                        if(getSpot(ia(i, j)) == getSpot(ia(q, r))){
                            c++;
                        }


                    }
                }



            }
        }
        return c;
    }
}

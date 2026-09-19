package LowLevelDesign.ConnectFour;

import LowLevelDesign.ConnectFour.enums.DiscColor;
import LowLevelDesign.ConnectFour.enums.GameState;
import LowLevelDesign.ConnectFour.model.Board;
import LowLevelDesign.ConnectFour.model.Player;

public class Game {
    private final Player[] players;
    private int currentPlayer;
    private final Board board;
    private GameState state;

    public Game(Player players[], int rows, int columns, int target){
        this.players = players;
        this.currentPlayer=  0;
        this.board = new Board(rows, columns, target);
        this.state = GameState.READY;
    }

    public void startGame() {
        setState(GameState.IN_PROGRESS);
    }

    public void makeMove(int column){
        if(this.state != GameState.IN_PROGRESS) {
            System.out.println("Invalid state of game");
            return;
        }

        if(!board.canPlace(column)){
            System.out.println("No empty rows to place in this column try for new column");
            return;
        }

        DiscColor color = players[currentPlayer].getColor();
        int row = board.placeDisc(column,color );
        if(board.checkWin(row, column, color)){
            setState(GameState.WON);
            System.out.println("Player "+ players[currentPlayer].getName() + " won the game");
            return;
        }

        if(board.isFull()){
            setState(GameState.DRAW);
            System.out.println("Game draw");
            return;
        }

        nextTurn();
    }

    private void nextTurn(){
         this.currentPlayer=(++currentPlayer)%players.length;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    private void setState(GameState state) {
        this.state=state;
    }

    public GameState getState() {
        return state;
    }

    public Player getWinner() {
        return state == GameState.WON ? players[currentPlayer] : null;
    }

}

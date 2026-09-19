package LowLevelDesign.SnakeAndLadder;

import LowLevelDesign.SnakeAndLadder.enums.GameState;

import java.util.List;
import java.util.Map;

public class GameBoard {
    final int winningScore = 100;
    private final Map<Integer, Integer> snakes;
    private final Map<Integer, Integer> ladder;
    private int currentPlayingPlayer;
    private final List<Player> players;
    private GameState gameState;

    public GameBoard(Map<Integer, Integer> snakes, Map<Integer, Integer> ladder, List<Player> players) {
        this.snakes = snakes;
        this.ladder = ladder;
        this.currentPlayingPlayer = 0;
        this.players = players;
        this.gameState = GameState.IN_PROGRESS;
    }

    

    public void startGame() {
        while (this.gameState == GameState.IN_PROGRESS) play();
        System.out.println("Player "+ players.get(currentPlayingPlayer).getName() + " won the match!");
    }

    public GameState getGameState(){
        return this.gameState;
    }

    private void play(){
        Player player = players.get(currentPlayingPlayer);
        int move = (int)(Math.random()*6)+1;
        int newPosition = player.getPosition()+move;
        if(newPosition>=winningScore){
            gameState = GameState.WON;
            return;
        }

        if(snakes.containsKey(newPosition)){
            newPosition = snakes.get(newPosition);
        }

        if(ladder.containsKey(newPosition)){
            newPosition = ladder.get(newPosition);
        }
        player.setPosition(newPosition);
        turn();
    }

    private void turn(){
        currentPlayingPlayer = currentPlayingPlayer ==  players.size()-1 ? 0: currentPlayingPlayer+1;
    }

}

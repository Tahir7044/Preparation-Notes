package LowLevelDesign.SnakeAndLadder;

import java.util.List;
import java.util.Map;

public class GameBoard {
    final int winningScore = 100;
    private final Map<Integer, Integer> snakes;
    private final Map<Integer, Integer> ladder;
    private int currentPlayingPlayer;
    private final List<Player> players;
    private boolean gameOver;

    public GameBoard(Map<Integer, Integer> snakes, Map<Integer, Integer> ladder, List<Player> players) {
        this.snakes = snakes;
        this.ladder = ladder;
        this.currentPlayingPlayer = 0;
        this.players = players;
        this.gameOver = false;
    }

    public void startGame() {
        while (!gameOver) play();
        System.out.println("Player "+ players.get(currentPlayingPlayer).getName() + " won the match!");
    }

    private void play(){
        Player player = players.get(currentPlayingPlayer);
        int move = (int)(Math.random()*6)+1;
        int newPosition = player.getPosition()+move;
        if(newPosition>=winningScore){
            gameOver = true;
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

package LowLevelDesign.SnakeAndLadder;

// Functional Requirements:
// 1. Two or more players take turns rolling a dice (1–6)
// 2. Player moves forward by the dice value from current position
// 3. Landing on a snake head → slide down to snake tail
// 4. Landing on a ladder base → climb up to ladder top
// 5. First player to reach or exceed position 100 wins
// 6. Game ends immediately when a winner is found

// Out of scope:
// - GUI / board rendering
// - Save/load game state
// - Network multiplayer

/*

Classes

- GameBoard
    - winningScore: int = 100
    - snakes: Map<Integer, Integer>   ← start(head) → end(tail), O(1) lookup
    - ladder: Map<Integer, Integer>   ← start(base) → end(top), O(1) lookup
    - players: List<Player>
    - currentPlayingPlayer: int       ← index into players list
    - gameOver: boolean
    + startGame()                     ← game loop: while(!gameOver) play()
    - play()                          ← one turn: roll dice, move, check snake/ladder/win
    - turn()                          ← rotate to next player

- Player
    - id: String
    - name: String
    - position: int                   ← starts at 0, wins at >= 100

Note: Coordinate.java is no longer needed — replaced by Map<Integer, Integer>

*/

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        // ===== Scenario 1: 2-player standard game =====
        System.out.println("===== Scenario 1: 2-player game =====");
        Map<Integer, Integer> snakes = Map.of(
                17, 7,
                54, 34,
                62, 19,
                64, 60,
                87, 24,
                93, 73,
                95, 75,
                99, 78
        );
        Map<Integer, Integer> ladders = Map.of(
                4,  14,
                9,  31,
                20, 38,
                28, 84,
                40, 59,
                51, 67,
                63, 81,
                71, 91
        );

        List<Player> players = List.of(
                new Player("P1", "Alice"),
                new Player("P2", "Bob")
        );

        GameBoard game = new GameBoard(snakes, ladders, players);
        game.startGame();

        // ===== Scenario 2: 3-player game =====
        System.out.println("\n===== Scenario 2: 3-player game =====");
        List<Player> players2 = List.of(
                new Player("P1", "Alice"),
                new Player("P2", "Bob"),
                new Player("P3", "Charlie")
        );

        GameBoard game2 = new GameBoard(snakes, ladders, players2);
        game2.startGame();

        // ===== Scenario 3: No snakes or ladders — pure dice game =====
        System.out.println("\n===== Scenario 3: No snakes or ladders =====");
        List<Player> players3 = List.of(
                new Player("P1", "Alice"),
                new Player("P2", "Bob")
        );

        GameBoard game3 = new GameBoard(Map.of(), Map.of(), players3);
        game3.startGame();
    }
}

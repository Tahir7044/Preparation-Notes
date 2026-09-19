package LowLevelDesign.ConnectFour;


/*

Requirements:

1 - Two players take turns dropping a disc into one of 7 columns on a 6-row board
2 - The disc falls to the lowest available row of the chosen column (gravity)
3 - The game ends when:
    3.1 - A player gets 4 discs in a row — vertical, horizontal, or diagonal (WON)
    3.2 - The board is full with no winner (DRAW)
4 - Invalid moves are rejected:
    4.1 - Dropping a disc into a full column
    4.2 - Making a move after the game is over (state != IN_PROGRESS)

Out of scope:
    UI / board rendering
    Concurrent games
    More than 2 players

Classes:

Player
    - name: String
    - color: DiscColor

Board
    - rows: int
    - columns: int
    - grid: DiscColor[][]
    - target: int                        ← win streak length (default 4)
    + canPlace(col): boolean
    + placeDisc(col, color): int         ← returns row where disc landed (bottom-up gravity)
    + isFull(): boolean
    + checkWin(row, col, color): boolean ← checks 4 directions from placed disc

Game
    - players: Player[]
    - currentPlayer: int                 ← index into players array
    - board: Board
    - state: GameState
    + startGame()                        ← transitions READY → IN_PROGRESS
    + makeMove(column)                   ← validates state, places disc, checks win/draw
    + getCurrentPlayer(): Player
    + getState(): GameState
    + getWinner(): Player                ← returns winner or null

Enums:
    DiscColor  { RED, BLUE }
    GameState  { READY, IN_PROGRESS, WON, DRAW }

*/

import LowLevelDesign.ConnectFour.enums.DiscColor;
import LowLevelDesign.ConnectFour.model.Player;

public class Main {
    public static void main(String[] args) {

        Player red  = new Player("Alice", DiscColor.RED);
        Player blue = new Player("Bob",   DiscColor.BLUE);

        // ===== Scenario 1: Horizontal win (RED wins on bottom row) =====
        System.out.println("===== Scenario 1: Horizontal win =====");
        Game g1 = new Game(new Player[]{red, blue}, 6, 7, 4);
        g1.startGame();
        g1.makeMove(0); // RED   col0
        g1.makeMove(0); // BLUE  col0
        g1.makeMove(1); // RED   col1
        g1.makeMove(1); // BLUE  col1
        g1.makeMove(2); // RED   col2
        g1.makeMove(2); // BLUE  col2
        g1.makeMove(3); // RED   col3 — horizontal win
        System.out.println("Winner: " + (g1.getWinner() != null ? g1.getWinner().getName() : "none"));

        // ===== Scenario 2: Vertical win (BLUE wins in column 0) =====
        System.out.println("\n===== Scenario 2: Vertical win =====");
        Game g2 = new Game(new Player[]{red, blue}, 6, 7, 4);
        g2.startGame();
        g2.makeMove(1); // RED
        g2.makeMove(0); // BLUE
        g2.makeMove(1); // RED
        g2.makeMove(0); // BLUE
        g2.makeMove(2); // RED
        g2.makeMove(0); // BLUE
        g2.makeMove(2); // RED
        g2.makeMove(0); // BLUE vertical win
        System.out.println("Winner: " + (g2.getWinner() != null ? g2.getWinner().getName() : "none"));

        // ===== Scenario 3: Move into full column (rejected) =====
        System.out.println("\n===== Scenario 3: Full column rejection =====");
        Game g3 = new Game(new Player[]{red, blue}, 2, 2, 4);
        g3.startGame();
        g3.makeMove(0); // RED
        g3.makeMove(0); // BLUE — column now full
        g3.makeMove(0); // RED  — should be rejected

        // ===== Scenario 4: Move after game over (rejected) =====
        System.out.println("\n===== Scenario 4: Move after game over =====");
        g1.makeMove(4); // g1 already WON — should be rejected
    }
}


package solution;

import battleship.BattleShip;

import java.awt.*;

public class ProbabilityBot {

    private int[][] gameBoard;
    private BattleShip battleShip;
    private final int NUMBEROFSIMULATEDGAMES;

    /**
     * Constructor just sets the number of games to simulate
     * @param n number of games
     */
    public ProbabilityBot(int n) {
        NUMBEROFSIMULATEDGAMES = n;


    }

    /**
     * Generates probability of n games
     */
    public void generateProbability() {
        gameBoard = new int[10][10];
        for (int i = 0; i < NUMBEROFSIMULATEDGAMES; i++) {
            battleShip = new BattleShip();

            while (!battleShip.allSunk()) {
                for (int col = 0; col < 10; col++) {
                    for (int row = 0; row < 10; row++) {
                        boolean hit = battleShip.shoot(new Point(row, col));
                        if (hit) {
                            gameBoard[row][col]++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Simple get method to return the grid
     * @return grid of probabilty simulated
     */
    public int[][] returnBoard() {
        return this.gameBoard;
    }
}

package solution;

import battleship.BattleShip;
import battleship.CellState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


/**
 * Starting code for Comp10152 - Lab#6
 *
 * @author mark.yendt
 *
 */
public class COMP10152_Lab6 {
    static final int NUMBEROFGAMES = 10000;

    public static void startingSolution() {
        double startTime = System.nanoTime();
        int totalShots = 0;
        ProbabilityBot probBot = new ProbabilityBot(100000); //Number of games to simulate
        probBot.generateProbability(); //Generate games
        int[][] board = probBot.returnBoard(); //Get results
        System.out.println(BattleShip.version());
        for (int game = 0; game < NUMBEROFGAMES; game++) {
            BattleShip battleShip = new BattleShip();
            EducatedBot eduBot = new EducatedBot(battleShip);
            eduBot.getProbability(board); //Gets a fresh copy of probability from the generated set
            int gameShots = eduBot.huntMode();
            totalShots += gameShots;
        }
        double stopTime = System.nanoTime();


        System.out.printf("SampleBot - The Average # of Shots required in %d games to sink all Ships = %.2f\n", NUMBEROFGAMES, (double) totalShots / NUMBEROFGAMES);
        System.out.printf("Process took %.2f Milliseconds to complete" , (stopTime - startTime) / 1000000);

    }


    public static void main(String[] args) {
        startingSolution();
    }
}

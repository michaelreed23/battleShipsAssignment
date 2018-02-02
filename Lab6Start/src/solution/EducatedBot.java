
package solution;

import battleship.BattleShip;
import java.awt.*;


public class EducatedBot {
    private BattleShip battleShip;
    private int boardSize;
    private Point target;
    private int[][] grid;
    private int sunkShips;

    /**
     * Smart bot to win battleships in the fewest number of moves
     * @param b battleship game passed in
     */
    public EducatedBot(BattleShip b) {
        battleShip = b; //Copy of battleship
        boardSize = b.BOARDSIZE; //BoardSize
        target = new Point(); //Class level point

        //Variables for the hunt mode


    }

    /**
     * Gets probability from the simulated games
     * @param grid Grid of probablities over n simulated games
     */
    public void getProbability(int[][] grid) {
        this.grid = new int[boardSize][boardSize];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                this.grid[i][j] = grid[i][j];
            }
        }
    }

    /**
     * Once a ship has been hit, targetMode is called and hunts until it sinks ship
     * @param lastHit The location where the hit took place from huntmode
     */
    private void targetMode(Point lastHit) {

        int[] directions = {0,0,0,0}; //probabilities for directions, by index, 0 is left, 1 is right, 2 is down, 3 is up

        boolean hit = false; //Sets to false initially
        if (lastHit.x - 1 >= 0) { //Sets to directions array to the probabilities around the target
            directions[0] = grid[lastHit.x - 1][lastHit.y];
        }
        if (lastHit.x + 1 < grid.length) {
            directions[1] = grid[lastHit.x + 1][lastHit.y];
        }
        if (lastHit.y + 1 < grid.length) {
            directions[2] = grid[lastHit.x][lastHit.y + 1];
        }
        if (lastHit.y - 1 >= 0)  {
            directions[3] = grid[lastHit.x][lastHit.y - 1];
        }
        Point fireLeft = new Point(lastHit.x - 1, lastHit.y); //Sets four points to around the target
        Point fireRight = new Point(lastHit.x + 1, lastHit.y);
        Point fireDown = new Point(lastHit.x, lastHit.y + 1);
        Point fireUp = new Point(lastHit.x, lastHit.y - 1);
        while (!sunkShip()) {
        int largest = 0; //Index with highest number
        int score = 0; //score of index
        for (int i = 0; i < directions.length; i++) { //Finds the most likely direction to fire in, based on probability
            if (directions[i] > score) {
                score = directions[i];
                largest = i;
            }
        }
        if (score == 0) { //In rare cases all directions have 0 probability. Its so rare that I cannot figure out why it happens. Return if this is the case
            return;
        }

            switch (largest) { //Fires in the direction that is most likely, if miss, sets that direction to 0, if hit, takes probability from next cell in that direction
                case 0:
                    hit = fireShot(fireLeft);
                    if (hit && --fireLeft.x >= 0) {
                        directions[3] -= 200;
                        directions[2] -=200;
                        directions[0] = grid[fireLeft.x][fireLeft.y];
                    } else {
                        directions[0] = 0;
                    }
                    break;
                case 1:
                    hit = fireShot(fireRight);
                    if (hit && ++fireRight.x < grid.length) {
                        directions[3] -= 200;
                        directions[2] -=200;
                        directions[1] = grid[fireRight.x][fireRight.y];

                    } else {
                        directions[1] = 0;
                    }
                    break;
                case 2:
                    hit = fireShot(fireDown);
                    if (hit && ++fireDown.y < grid.length) {
                        directions[0] -= 200;
                        directions[1] -=200;
                        directions[2] = grid[fireDown.x][fireDown.y];

                    } else {
                        directions[2] = 0;
                    }
                    break;
                case 3:
                    hit = fireShot(fireUp);
                    if (hit && --fireUp.y >= 0) {
                        directions[0] -= 200;
                        directions[1] -=200;
                        directions[3] = grid[fireUp.x][fireUp.y];

                    } else {
                        directions[3] = 0;
                    }
                    break;
            }
        }
    }


    /**
     * First mode called of the class, continuously fires at the target with highest probability
     * @return number of shots to sink all ships
     */
    public int huntMode() {
        while (true) {
            int highestProbability = 0;
            for (int i = 0; i < grid.length; i++) {//Finds highest probability target
                for (int j = 0; j < grid.length; j++) {
                    if (grid[i][j] > highestProbability) {
                        highestProbability = grid[i][j];
                        target.setLocation(i, j);
                    }
                }
            }
            boolean hit = false; //Set to false initially
            if (grid[target.x][target.y] > 0) {
                hit = fireShot(target); //fires at highest prob target
            }

            if (hit) {
                targetMode(target); //If hit, activate targetmode to sink that ship
            }
            if (battleShip.allSunk()) {
                return battleShip.totalShotsTaken(); //returns number of shots taken for this game
            }
        }




    }

    /**
     * Fires at the Point parameter passed in
     * @param shot location of last shot
     * @return true if hit, false if miss
     */
    public boolean fireShot(Point shot) {
        boolean hit = battleShip.shoot(shot);
        grid[shot.x][shot.y] = 0; //Sets probability to 0 for the target regardless so we don't fire there again
        if (!hit) { //Adjusts probability if miss to load next shot
            adjustProb(shot);
        }
        return hit;

    }

    /**
     * checks if ship is sunk
     * @return true if sunk, false otherwise
     */
    private boolean sunkShip() {
        if (sunkShips < battleShip.numberOfShipsSunk()) {
            sunkShips++;

            return true;
        } else {
            return false;
        }
    }

    /**
     * Adjusts the probabilities of the positions around the parameter passed in
     * @param shot location of shot passed in
     */
    private void adjustProb(Point shot) {
        if (shot.x + 1 < grid.length) {
            grid[shot.x+1][shot.y] /= 5;
            if (shot.x + 2 < grid.length) {
                grid[shot.x+2][shot.y]/=2;
            }
        }
        if (shot.x - 1 > 0) {
            grid[shot.x - 1][shot.y] /= 5;
            if (shot.x - 2 > 0) {
                grid[shot.x-2][shot.y]/=2;
            }
        }
        if (shot.y + 1 < grid.length) {
            grid[shot.x][shot.y + 1] /= 5;
            if (shot.y + 2 < grid.length) {
                grid[shot.x][shot.y + 2]/=2;
            }
        }
        if (shot.y - 1 > 0) {
            grid[shot.x][shot.y - 1] /= 5;
            if (shot.y - 2 > 0) {
                grid[shot.x][shot.y - 2]/=2;
            }
        }

    }

}



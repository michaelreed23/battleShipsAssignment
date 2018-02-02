package solution;

import battleship.BattleShip;
import battleship.CellState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class SmartBot {


    private int boardSize;
    private BattleShip battleShip;
    private CellState[][] grid;
    private ArrayList<Integer> shipSizes;
    private Point target;
    private int numShipsSunk;
    private int hits;


    public SmartBot() {
        numShipsSunk = 0;

        battleShip = new BattleShip();
        boardSize = battleShip.BOARDSIZE;
        shipSizes = new ArrayList<>();
        for (int i : battleShip.shipSizes()) {
            shipSizes.add(i);
        }
        grid = new CellState[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid[i][j] = CellState.Empty;
            }
        }


        target = new Point();


    }

    public int huntMode() {
        boolean hit = false;
        while (!battleShip.allSunk()) {
            for (int i = 0; i < grid.length; i++) {
                int j = 0;
                if (i % 2 == 0) {
                    j = Collections.min(shipSizes);
                }
                for (j = j; j < grid.length; j+=Collections.min(shipSizes)) {
                    target.setLocation(i, j);
                    if (grid[target.x][target.y] == CellState.Empty) {
                        hit = shoot(target);

                    }
                    if (hit) {
                        targetMode(target);
                    }
                    if (battleShip.allSunk()) {
                        return battleShip.totalShotsTaken();
                    }
                }
            }
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid.length; j++) {
                    if (grid[i][j] == CellState.Empty) {
                        shoot(new Point(i,j));
                    }
                }
            }

        }
        return battleShip.totalShotsTaken();
    }

    public void targetMode(Point lastHit) {
        boolean hit = false;
        for (int i = lastHit.x + 1; i < lastHit.x + 5; i++) {
            if (i < boardSize && grid[i][lastHit.y] == CellState.Empty) {
                hit = shoot(new Point(i, lastHit.y));
                if (hit) {
                    if (sunkShip()) {
                        return;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = lastHit.x - 1; i > lastHit.x - 5; i--) {
            if (i >= 0 && grid[i][lastHit.y] == CellState.Empty) {
                hit = shoot(new Point(i, lastHit.y));
                if (hit) {
                    if (sunkShip()) {
                        return;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = lastHit.y + 1; i < lastHit.y + lastHit.y + 5; i++) {
            if (i < boardSize && grid[lastHit.x][i] == CellState.Empty) {
                hit = shoot(new Point(lastHit.x, i));
                if (hit) {
                    if (sunkShip()) {
                        return;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = lastHit.y - 1; i > lastHit.y - 5; i--) {
            if (i >= 0 && grid[lastHit.x][i] == CellState.Empty) {
                hit = shoot(new Point(lastHit.x, i));
                if (hit) {
                    if (sunkShip()) {
                        return;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private boolean shoot(Point shot) {
        boolean hit = battleShip.shoot(shot);

        if (hit) {
            hits++;
            grid[shot.x][shot.y] = CellState.Hit;

            return true;
        } else {
            grid[shot.x][shot.y] = CellState.Miss;

            return false;
        }

    }

    private boolean sunkShip() {
        if (numShipsSunk < battleShip.numberOfShipsSunk()) {
            int index = shipSizes.indexOf(hits);
            if (index > -1) {
                shipSizes.remove(index);
            }
            hits = 0;
            numShipsSunk++;
            return true;
        } else {
            return false;
        }
    }


}





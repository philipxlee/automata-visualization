package cellsociety.model.Variations;

import cellsociety.model.Cell;
import cellsociety.model.Simulation;
import java.util.List;
import java.util.Random;

class SpreadingOfFire implements Simulation {

  private final double CATCH_FIRE_PROBABILITY = 0.15;
  Random rand = new Random();

  /**
   * Calculates the next state of a cell in based on its current state and neighboring cells.
   * @param cell The cell whose next state is being determined.
   * @param currentState The current state of the cell, one of "BURNING", "EMPTY", or "TREE".
   * @param neighbors A list of neighboring cells to the current cell.
   * @return The next state of the cell as a String: "BURNING", "EMPTY", or "TREE".
   *         "BURNING" if cell is a tree with burning neighbors and catches fire based on a probability.
   *         "EMPTY" if cell is currently "BURNING" or "EMPTY".
   *         The state remains "TREE" if conditions to catch fire are not met.
   * @throws IllegalStateException If the current state is not one of the expected values.
   */
  @Override
  public String determineState(Cell cell, String currentState, List<Cell> neighbors) {
    if (currentState.equals("BURNING") || currentState.equals("EMPTY")) {
      return "EMPTY";
    }
    if (currentState.equals("TREE")) {
      int[] treesAndBurnCount = countTreeAndBurningNeighbors(cell, neighbors);
      boolean hasBurningNeighbor = treesAndBurnCount[1] > 0;
      return hasBurningNeighbor && (rand.nextDouble() < CATCH_FIRE_PROBABILITY) ? "BURNING" : "TREE";
    }
    throw new IllegalStateException("Unexpected cell state: " + currentState);
  }


  private int[] countTreeAndBurningNeighbors(Cell cell, List<Cell> neighbors) {
    int trees = 0;
    int burning = 0;
    for (Cell neighbor : neighbors) {
      if (isCardinalNeighbor(cell, neighbor)) {
        int tree = neighbor.getState().equals("TREE") ? 1 : 0;
        int burn = neighbor.getState().equals("BURNING") ? 1 : 0;
        trees += tree;
        burning += burn;
      }
    }
    return new int[]{trees, burning};
  }

  private boolean isCardinalNeighbor(Cell centralCell, Cell neighbor) {
    // Check if the neighbor is directly north, south, east, or west of the central cell
    boolean sameRow = centralCell.getRow() == neighbor.getRow();
    boolean sameCol = centralCell.getCol() == neighbor.getCol();
    boolean adjacentRow = Math.abs(centralCell.getRow() - neighbor.getRow()) == 1;
    boolean adjacentCol = Math.abs(centralCell.getCol() - neighbor.getCol()) == 1;
    return (sameRow && adjacentCol) || (sameCol && adjacentRow);
  }
}





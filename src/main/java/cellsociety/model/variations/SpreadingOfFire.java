package cellsociety.model.variations;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.SpreadingOfFireCell;
import java.util.List;
import java.util.Random;

public class SpreadingOfFire implements Simulation<SpreadingOfFireCell> {

  private final double CATCH_FIRE_PROBABILITY = 0.15;
  private final String BURNING = CellStates.BURNING.name();
  private final String EMPTY = CellStates.EMPTY.name();
  private final String TREE = CellStates.TREE.name();
  private final Random rand = new Random();

  /**
   * Creates a new SpreadingOfFireCell with specified row, column, and state.
   * @param row The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
   * @param state The initial state of the cell, such as "TREE", "FIRE", or "EMPTY".
   * @return A new instance of SpreadingOfFireCell with the given parameters.
   */
  @Override
  public SpreadingOfFireCell createVariationCell(int row, int col, String state) {
    return new SpreadingOfFireCell(row, col, state);
  }

  /**
   * Calculates the next state of a cell in based on its current state and neighboring cells.
   * @param cell The cell whose next state is being determined.
   * @param currentState The current state of the cell, one of "BURNING", "EMPTY", or "TREE".
   * @param neighbors A list of neighboring cells to the current cell.
   * @return The next state of the cell as a String: "BURNING", "EMPTY", or "TREE".
   *    "BURNING" if cell is a tree with burning neighbors and catches fire based on a probability.
   *    "EMPTY" if cell is currently "BURNING" or "EMPTY".
   *    The state remains "TREE" if conditions to catch fire are not met.
   * @throws IllegalStateException If the current state is not one of the expected values.
   */
  @Override
  public String determineState(SpreadingOfFireCell cell, String currentState, List<SpreadingOfFireCell> neighbors) {
    if (currentState.equals(BURNING) || currentState.equals(EMPTY)) {
      return EMPTY;
    }
    if (currentState.equals(TREE)) {
      int[] treesAndBurnCount = countTreeAndBurningNeighbors(cell, neighbors);
      boolean hasBurningNeighbor = treesAndBurnCount[1] > 0;
      return hasBurningNeighbor && (rand.nextDouble() < CATCH_FIRE_PROBABILITY) ? BURNING : TREE;
    }
    throw new IllegalStateException("Unexpected cell state: " + currentState);
  }


  private int[] countTreeAndBurningNeighbors(SpreadingOfFireCell cell, List<SpreadingOfFireCell> neighbors) {
    int trees = 0;
    int burning = 0;
    for (SpreadingOfFireCell neighbor : neighbors) {
      if (isCardinalNeighbor(cell, neighbor)) {
        int tree = neighbor.getState().equals(TREE) ? 1 : 0;
        int burn = neighbor.getState().equals(BURNING) ? 1 : 0;
        trees += tree;
        burning += burn;
      }
    }
    return new int[]{trees, burning};
  }

  private boolean isCardinalNeighbor(SpreadingOfFireCell centralCell, SpreadingOfFireCell neighbor) {
    // Check if the neighbor is directly north, south, east, or west of the central cell
    boolean sameRow = centralCell.getRow() == neighbor.getRow();
    boolean sameCol = centralCell.getCol() == neighbor.getCol();
    boolean adjacentRow = Math.abs(centralCell.getRow() - neighbor.getRow()) == 1;
    boolean adjacentCol = Math.abs(centralCell.getCol() - neighbor.getCol()) == 1;
    return (sameRow && adjacentCol) || (sameCol && adjacentRow);
  }

}






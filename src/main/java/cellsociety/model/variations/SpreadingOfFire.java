package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.BasicCell;
import java.util.List;
import java.util.Random;

public class SpreadingOfFire implements Simulation<BasicCell> {

  private final double CATCH_FIRE_PROBABILITY = 0.15;
  private final String BURNING = CellStates.BURNING.name();
  private final String EMPTY = CellStates.EMPTY.name();
  private final String TREE = CellStates.TREE.name();
  private final Random rand = new Random();

  /**
   * Creates a new SpreadingOfFireCell with specified row, column, and state.
   *
   * @param row   The row position of the cell in the grid.
   * @param col   The column position of the cell in the grid.
   * @param state The initial state of the cell, such as "TREE", "FIRE", or "EMPTY".
   * @return A new instance of SpreadingOfFireCell with the given parameters.
   */
  @Override
  public BasicCell createVariationCell(int row, int col, String state) {
    return new BasicCell(row, col, state);
  }


  @Override
  public void prepareCellNextState(BasicCell cell, List<BasicCell> neighbors) {
    String currentState = cell.getState();
    String nextState = currentState; // Default to current state

    if (currentState.equals(BURNING) || currentState.equals(EMPTY)) {
      nextState = EMPTY;
    } else if (currentState.equals(TREE)) {
      boolean hasBurningNeighbor = checkForBurningNeighbor(cell, neighbors);
      if (hasBurningNeighbor && rand.nextDouble() < CATCH_FIRE_PROBABILITY) { // randomless error
        nextState = BURNING;
      }
    }
    cell.setNextState(nextState);
  }

  private boolean checkForBurningNeighbor(BasicCell cell, List<BasicCell> neighbors) {
    boolean hasBurningNeighbor = false;
    for (BasicCell neighbor : neighbors) {
      if (isCardinalNeighbor(cell, neighbor)) {
        if (neighbor.getState().equals(BURNING)) {
          hasBurningNeighbor = true;
          break; // Exit the loop as soon as a burning neighbor is found
        }
      }
    }
    return hasBurningNeighbor;
  }

  private boolean isCardinalNeighbor(BasicCell centralCell, BasicCell neighbor) {
    // Check if the neighbor is directly north, south, east, or west of the central cell
    boolean sameRow = centralCell.getRow() == neighbor.getRow();
    boolean sameCol = centralCell.getCol() == neighbor.getCol();
    boolean adjacentRow = Math.abs(centralCell.getRow() - neighbor.getRow()) == 1;
    boolean adjacentCol = Math.abs(centralCell.getCol() - neighbor.getCol()) == 1;
    return (sameRow && adjacentCol) || (sameCol && adjacentRow);
  }

}






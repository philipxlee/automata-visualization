package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.BasicCell;
import java.util.List;
import java.util.Random;

public class SpreadingOfFire implements Simulation<BasicCell> {

  private static final String BURNING = CellStates.BURNING.name();
  private static final String EMPTY = CellStates.EMPTY.name();
  private static final String TREE = CellStates.TREE.name();
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


  /**
   * Determines the next state of a cell based on its current state and the states of its neighbors.
   * This method implements the rules of Spreading of Fire simulation: - A "BURNING" cell becomes
   * "EMPTY" in the next state. - An "EMPTY" cell becomes a "TREE" with a small probability. - A
   * "TREE" cell becomes "BURNING" if any of its cardinal neighbors are "BURNING" or with a small
   * probability.
   *
   * @param cell The cell whose next state is to be determined.
   * @param neighbors A list of the cell's neighbors, used to check if any are "BURNING"
   */
  @Override
  public void prepareCellNextState(BasicCell cell, List<BasicCell> neighbors) {
    String currentState = cell.getState();
    String nextState = currentState; // Default to current state
    double BECOME_TREE_PROBABILITY = 0.01;
    if (currentState.equals(BURNING)) {
      nextState = EMPTY;
    } else if (currentState.equals(EMPTY) && rand.nextDouble() < BECOME_TREE_PROBABILITY) {
      nextState = TREE;
    } else if (currentState.equals(TREE)) {
      boolean hasBurningNeighbor = checkForBurningNeighbor(cell, neighbors);
      double CATCH_FIRE_PROBABILITY = 0.01;
      if (hasBurningNeighbor || rand.nextDouble() < CATCH_FIRE_PROBABILITY) {
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
          break;
        }
      }
    }
    return hasBurningNeighbor;
  }

  private boolean isCardinalNeighbor(BasicCell centralCell, BasicCell neighbor) {
    boolean sameRow = centralCell.getRow() == neighbor.getRow();
    boolean sameCol = centralCell.getCol() == neighbor.getCol();
    boolean adjacentRow = Math.abs(centralCell.getRow() - neighbor.getRow()) == 1;
    boolean adjacentCol = Math.abs(centralCell.getCol() - neighbor.getCol()) == 1;
    return (sameRow && adjacentCol) || (sameCol && adjacentRow);
  }

}






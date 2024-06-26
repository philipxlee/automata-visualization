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
  private static final String TREE_GROWTH_CHANCE = "probabilityProduction";
  private static final String CATCH_FIRE_CHANCE = "probabilityFire";
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
   * @param cell      The cell whose next state is to be determined.
   * @param neighbors A list of the cell's neighbors, used to check if any are "BURNING"
   */
  @Override
  public void prepareCellNextState(BasicCell cell, List<BasicCell> neighbors) {
    String currentState = cell.getState();
    String nextState = currentState; // Default to current state
    parameters.putIfAbsent(TREE_GROWTH_CHANCE, 0.01);
    double becomeTreeProbability = parameters.get(TREE_GROWTH_CHANCE);
    if (currentState.equals(BURNING)) {
      nextState = EMPTY;
    } else if (currentState.equals(EMPTY) && rand.nextDouble() < becomeTreeProbability) {
      nextState = TREE;
    } else if (currentState.equals(TREE)) {
      parameters.putIfAbsent(CATCH_FIRE_CHANCE, 0.01);
      boolean hasBurningNeighbor = checkForBurningNeighbor(cell, neighbors);
      double catchFireProbability = parameters.get(CATCH_FIRE_CHANCE);
      if (hasBurningNeighbor || rand.nextDouble() < catchFireProbability) {
        nextState = BURNING;
      }
    }
    cell.setNextState(nextState);
  }

  private boolean checkForBurningNeighbor(BasicCell cell, List<BasicCell> neighbors) {
    for (BasicCell neighbor : neighbors) {
      if (isCardinalNeighbor(cell, neighbor)) {
        if (neighbor.getState().equals(BURNING)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isCardinalNeighbor(BasicCell centralCell, BasicCell neighbor) {
    boolean sameRow = centralCell.getRow() == neighbor.getRow();
    boolean sameCol = centralCell.getCol() == neighbor.getCol();
    boolean adjacentRow = Math.abs(centralCell.getRow() - neighbor.getRow()) == 1;
    boolean adjacentCol = Math.abs(centralCell.getCol() - neighbor.getCol()) == 1;
    return (sameRow && adjacentCol) || (sameCol && adjacentRow);
  }

}






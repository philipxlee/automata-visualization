package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.BasicCell;
import java.util.List;

public class Percolation implements Simulation<BasicCell> {

  private static final String PERCOLATED = CellStates.PERCOLATED.name();
  private static final String EMPTY = CellStates.EMPTY.name();

  /**
   * Creates a new PercolationCell with specified row, column, and state.
   *
   * @param row The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
   * @param state The initial state of the cell, usually "PERCOLATED" or "EMPTY".
   * @return A new instance of PercolationCell with the given parameters.
   */
  @Override
  public BasicCell createVariationCell(int row, int col, String state) {
    return new BasicCell(row, col, state);
  }

  /**
   * Determines the next state of a cell based on its current state and the states of its neighbors.
   * This method implements the rules of Percolation: - An "PERCOLATED" cell will make all of its
   * "EMPTY" neighbors "PERCOLATED". If the cell's current state does not match "PERCOLATED" or
   * @param cell The cell whose next state is to be determined.
   * @param neighbors A list of the cell's neighbors, used to count how many are "PERCOLATED"
   */
  @Override
  public void prepareCellNextState(BasicCell cell, List<BasicCell> neighbors) {
    if (cell.getState().equals(PERCOLATED)) {
      for (BasicCell neighbor : neighbors) {
        if (neighbor.getState().equals(EMPTY)) {
          neighbor.setNextState(PERCOLATED);
        }
      }
    }
  }
}

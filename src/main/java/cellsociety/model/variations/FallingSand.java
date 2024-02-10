package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.BasicCell;
import java.util.List;

public class FallingSand implements Simulation<BasicCell> {

  private static final String SAND = CellStates.SAND.name();
  private static final String WALL = CellStates.WALL.name();
  private static final String EMPTY = CellStates.EMPTY.name();

  /**
   * Create a new cell with the given state at the given row and column.
   *
   * @param row The row of the cell.
   * @param col The column of the cell.
   * @param state The state of the cell.
   * @return A new cell with the given state at the given row and column.
   */
  @Override
  public BasicCell createVariationCell(int row, int col, String state) {
    return new BasicCell(row, col, state);
  }

  /**
   * Prepare the next state of the given cell based on its current state and the states of its
   * neighbors. This method implements the rules of the Falling Sand simulation: - A "SAND" cell
   * falls down if there is an empty cell below it. - A "SAND" cell does not move if a "WALL"
   * or another "SAND" cell is below it.
   *
   * @param cell The cell whose next state is to be determined.
   * @param neighbors A list of the cell's neighbors.
   */
  @Override
  public void prepareCellNextState(BasicCell cell, List<BasicCell> neighbors) {
    boolean hasSouthNeighbor = false;
    BasicCell southNeighbor = cell;
    for (BasicCell neighbor : neighbors) {
      if (hasSouthNeighbor(cell, neighbor)) {
        hasSouthNeighbor = true;
        southNeighbor = neighbor;
        break;
      }
    }
    handleSandFall(cell, hasSouthNeighbor, southNeighbor);
  }

  private void handleSandFall(BasicCell cell, boolean hasSouthCell, BasicCell southCell) {
    if (cell.getState().equals(SAND) && hasSouthCell) {
      if (southCell.getState().equals(EMPTY)) {
        cell.setNextState(EMPTY);
        southCell.setNextState(SAND);
      } else if (southCell.getState().equals(WALL) || southCell.getState().equals(SAND)) {
        cell.setNextState(SAND);
      }
    }
  }

  private boolean hasSouthNeighbor(BasicCell centralCell, BasicCell neighbor) {
    boolean sameCol = centralCell.getCol() == neighbor.getCol();
    boolean southRow = neighbor.getRow() == centralCell.getRow() + 1;
    return sameCol && southRow;
  }
}

package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.SandCell;
import java.util.List;
import java.util.Random;

public class FallingSand implements Simulation<SandCell> {

  private static final String SAND = CellStates.SAND.name();
  private static final String WALL = CellStates.WALL.name();
  private static final String EMPTY = CellStates.EMPTY.name();
  private Random rand = new Random();

  /**
   * Creates a new SandCell with specified row, column, and state.
   *
   * @param row The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
   * @param state The initial state of the cell, usually "SAND", "WALL", or "EMPTY".
   * @return A new instance of SandCell with the given parameters.
   */
  @Override
  public SandCell createVariationCell(int row, int col, String state) {
    return new SandCell(row, col, state);
  }

  /**
   * Determines the next state of a cell based on its current state and the states of its neighbors.
   * This method implements the rules of FallingSand: - A "SAND" cell falls to an empty cell below
   * it if there is one available. - A "SAND" cell falls to an empty cell diagonally below it if
   * there is one available. - A "SAND" cell does not move if there are no empty cells below it.
   *
   * @param cell The cell whose next state is to be determined.
   * @param neighbors A list of the cell's neighbors, used to check if any are "EMPTY" or "WALL"
   */
  @Override
  public void prepareCellNextState(SandCell cell, List<SandCell> neighbors) {
    handleSandFall(cell, neighbors);
  }

  private void handleSandFall(SandCell cell, List<SandCell> neighbors) {
    if (!cell.getState().equals(SAND)) {
      return;
    }
    SandCell below = getNeighbor(cell, neighbors, 1, 0);
    if (below != null && below.getState().equals(EMPTY)) {
      swapStates(cell, below);
      return;
    }
    if (below != null && (below.getState().equals(WALL) ||
        (below.getState().equals(SAND) && below.getStacked()))) {
      attemptDiagonalMove(cell, neighbors);
    }
  }

  private void attemptDiagonalMove(SandCell cell, List<SandCell> neighbors) {
    SandCell leftDiagonal = getNeighbor(cell, neighbors, 1, -1);
    SandCell rightDiagonal = getNeighbor(cell, neighbors, 1, 1);
    boolean canMoveLeft = leftDiagonal != null && leftDiagonal.getState().equals(EMPTY);
    boolean canMoveRight = rightDiagonal != null && rightDiagonal.getState().equals(EMPTY);
    if (canMoveLeft && (!canMoveRight || rand.nextBoolean())) {
      swapStates(cell, leftDiagonal);
    } else if (canMoveRight) {
      swapStates(cell, rightDiagonal);
    } else {
      cell.setStacked(true); // if no diagonal is possible
    }
  }

  private void swapStates(SandCell source, SandCell target) {
    target.setNextState(SAND);
    source.setNextState(EMPTY);
  }

  private SandCell getNeighbor(SandCell cell, List<SandCell> neighbors, int rOffset, int cOffset) {
    int targetRow = cell.getRow() + rOffset;
    int targetCol = cell.getCol() + cOffset;
    for (SandCell neighbor : neighbors) {
      if (neighbor.getRow() == targetRow && neighbor.getCol() == targetCol) {
        return neighbor;
      }
    }
    return null; // No neighbor found at the specified location
  }

}

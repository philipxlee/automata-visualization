package cellsociety.model.variations;

import cellsociety.model.Simulation;
import cellsociety.model.celltypes.BasicCell;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Schelling implements Simulation<BasicCell> {

  private static final String EMPTY = "EMPTY";
  private final Queue<BasicCell> emptyCells = new LinkedList<>();

  /**
    * Create a new cell with the given state at the given row and column
    *
    * @param row The row position of the cell in the grid
    * @param col The column position of the cell in the grid
    * @param state The initial state of the cell, usually "X" or "O"
    * @return A new instance of BasicCell with the given parameters
   */
  @Override
  public BasicCell createVariationCell(int row, int col, String state) {
    BasicCell cell = new BasicCell(row, col, state);
    if (EMPTY.equals(state)) {
      emptyCells.add(cell);
    }
    return cell;
  }

  /**
   * Determines the next state of a cell based on its current state and the states of its neighbors.
   * This method implements the rules of Schelling's segregation model: - A cell is "satisfied" if
   * at least THRESHOLD of its neighbors are of the same state. - If a cell is not satisfied,
   * it moves to an empty space if one is available. - If a cell is satisfied, it stays in its
   * current state.
   *
   * @param cell The cell whose next state is to be determined
   * @param neighbors A list of the cell's neighbors, used to count how many are "X" and "O"
   */
  @Override
  public void prepareCellNextState(BasicCell cell, List<BasicCell> neighbors) {
    String currentState = cell.getState();
    if (!currentState.equals(EMPTY)) {
      int[] counts = countNeighborsStates(neighbors);
      boolean satisfied = calculateSatisfaction(currentState, counts);
      if (!satisfied) {
        moveToEmptySpaceIfAvailable(cell);
      } else {
        cell.setNextState(currentState);
      }
    }
  }

  private boolean calculateSatisfaction(String currentState, int[] counts) {
    int sameStateCount = "X".equals(currentState) ? counts[0] : counts[1];
    int otherStateCount = "X".equals(currentState) ? counts[1] : counts[0];
    int totalCount = sameStateCount + otherStateCount;
    double THRESHOLD = parameters.get("threshold");
    return (otherStateCount == 0 || (double) sameStateCount / (totalCount) >= THRESHOLD);
  }

  private int[] countNeighborsStates(List<BasicCell> neighbors) {
    int x = 0;
    int o = 0;
    for (BasicCell neighbor : neighbors) {
      int xCount = "X".equals(neighbor.getState()) ? 1 : 0;
      int oCount = "O".equals(neighbor.getState()) ? 1 : 0;
      x += xCount;
      o += oCount;
    }
    return new int[]{x, o};
  }

  private void moveToEmptySpaceIfAvailable(BasicCell cell) {
    if (!emptyCells.isEmpty()) {
      BasicCell emptyCell = emptyCells.poll();
      emptyCell.setNextState(cell.getState());
      cell.setNextState(EMPTY);
      emptyCells.add(cell); // Add the now-empty cell back to the queue
    }
  }
}

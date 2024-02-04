package cellsociety.model.variations;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.SchellingCell;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Schelling implements Simulation<SchellingCell> {

  private final double THRESHOLD = 0.30;
  private final String EMPTY = CellStates.EMPTY.name();
  private final Queue<SchellingCell> emptyCells = new LinkedList<>();

  /**
   * Creates a new SchellingCell with specified row, column, and state.
   * @param row The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
   * @param state The initial state of the cell, indicating the type of agent.
   * @return A new instance of SchellingCell with the given parameters.
   */
  @Override
  public SchellingCell createVariationCell(int row, int col, String state) {
    return new SchellingCell(row, col, state);
  }

  /**
   * Determines the next state of a cell based on its current state and satisfaction level.
   * If a cell ('X' or 'O') is unsatisfied, it attempts to move to an empty space. If no
   * movement occurs (due to lack of empty spaces), the cell's state remains the same. If the
   * movement is possible, the cell becomes "EMPTY" and the empty cell becomes the current state.
   * @param cell The cell whose next state is being determined.
   * @param currentState The current state ('X', 'O', or 'EMPTY') of the cell.
   * @param neighbors A list of the cell's neighboring cells.
   * @return The new state of the cell ('X', 'O', or 'EMPTY') after applying movement rules.
   * @throws IllegalStateException If the current state is not recognized.
   */
  @Override
  public String determineState(SchellingCell cell, String currentState, List<SchellingCell> neighbors) {
    if (!"X".equals(currentState) && !"O".equals(currentState)) {
      throw new IllegalStateException("Unexpected cell state: " + currentState);
    }
    int[] counts = countNeighborsStates(neighbors);
    boolean satisfied = calculateSatisfaction(currentState, counts);
    return satisfied ? currentState : moveToEmptySpaceIfAvailable(currentState);
  }

  private boolean calculateSatisfaction(String currentState, int[] counts) {
    int sameStateCount = "X".equals(currentState) ? counts[0] : counts[1];
    int otherStateCount = "X".equals(currentState) ? counts[1] : counts[0];
    return (otherStateCount == 0 || (double) sameStateCount / (otherStateCount) >= THRESHOLD); // Avoid division by zero
  }

  private int[] countNeighborsStates(List<SchellingCell> neighbors) {
    int x = 0;
    int o = 0;
    for (SchellingCell neighbor : neighbors) {
      int xNeighbor = neighbor.getState().equals("X") ? 1 : 0;
      int oNeighbor = neighbor.getState().equals("O") ? 1 : 0;
      x += xNeighbor;
      o += oNeighbor;
      if (neighbor.getState().equals(EMPTY)) {
        emptyCells.add(neighbor);
      }
    }
    return new int[]{x, o};
  }

  private String moveToEmptySpaceIfAvailable(String cellState) {
    if (!emptyCells.isEmpty()) {
      SchellingCell emptyCell = emptyCells.poll();
      emptyCell.setState(cellState);
      return EMPTY;
    }
    return cellState;
  }
}

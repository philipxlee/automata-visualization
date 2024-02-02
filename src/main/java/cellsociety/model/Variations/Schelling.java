package cellsociety.model.Variations;

import cellsociety.model.Simulation;
import cellsociety.model.Cell;
import cellsociety.model.VariationCells.GameOfLifeCell;
import cellsociety.model.VariationCells.SchellingCell;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Schelling implements Simulation {

  private final double THRESHOLD = 0.30;
  private final Queue<Cell> emptyCells = new LinkedList<>();

  @Override
  public Cell createVariationCell(int row, int col, String state) {
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
  public String determineState(Cell cell, String currentState, List<Cell> neighbors) {
    int[] countOfXandONeighbors = countNeighborsStates(cell, neighbors);
    boolean satisfied = calculateSatisfaction(currentState, countOfXandONeighbors);
    if ("X".equals(currentState) || "O".equals(currentState)) {
      if (satisfied) {
        return currentState;
      } else {
        return moveToEmptySpaceIfAvaliable(currentState) ? "EMPTY" : currentState;
      }
    }
    throw new IllegalStateException("Unexpected cell state: " + currentState);
  }

  private boolean calculateSatisfaction(String currentState, int[] countOfXandONeighbors) {
    if (currentState.equals("X")) {
      return (double) countOfXandONeighbors[0] / countOfXandONeighbors[1] >= THRESHOLD;
    } else if (currentState.equals("O")) {
      return (double) countOfXandONeighbors[1] / countOfXandONeighbors[0] >= THRESHOLD;
    }
    return true;
  }

  private int[] countNeighborsStates(Cell cell, List<Cell> neighbors) {
    int x = 0;
    int o = 0;
    for (Cell neighbor : neighbors) {
      int xNeighbor = neighbor.getState().equals("X") ? 1 : 0;
      int oNeighbor = neighbor.getState().equals("O") ? 1 : 0;
      x += xNeighbor;
      o += oNeighbor;
      if (neighbor.getState().equals("EMPTY")) {
        emptyCells.add(neighbor);
      }
    }
    return new int[]{x, o};
  }

  private boolean moveToEmptySpaceIfAvaliable(String cellState) {
    if (!emptyCells.isEmpty()) {
      Cell emptyCell = emptyCells.poll();
      emptyCell.setState(cellState);
      return true;
    }
    return false;
  }
}

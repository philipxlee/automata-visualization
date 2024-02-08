package cellsociety.model.variations;

import cellsociety.model.Simulation;
import cellsociety.model.celltypes.BasicCell;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Schelling implements Simulation<BasicCell> {

  private final String EMPTY = "EMPTY";
  private final Queue<BasicCell> emptyCells = new LinkedList<>();

  @Override
  public BasicCell createVariationCell(int row, int col, String state) {
    BasicCell cell = new BasicCell(row, col, state);
    if (EMPTY.equals(state)) {
      emptyCells.add(cell);
    }
    return cell;
  }

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
    double THRESHOLD = 0.30;
    return (otherStateCount == 0 || (double) sameStateCount / (otherStateCount + sameStateCount) >= THRESHOLD);
  }

  private int[] countNeighborsStates(List<BasicCell> neighbors) {
    int x = 0, o = 0;
    for (BasicCell neighbor : neighbors) {
      if ("X".equals(neighbor.getState())) x++;
      if ("O".equals(neighbor.getState())) o++;
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

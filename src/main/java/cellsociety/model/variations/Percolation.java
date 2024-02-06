package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.celltypes.BasicCell;
import cellsociety.model.Simulation;
import java.util.List;

public class Percolation implements Simulation<BasicCell> {

  private final String PERCOLATED = CellStates.PERCOLATED.name();
  private final String EMPTY = CellStates.EMPTY.name();
  private final String WALL = CellStates.WALL.name();

  @Override
  public BasicCell createVariationCell(int row, int col, String state) {
    return new BasicCell(row, col, state);
  }

  // Corrected method signature to match the interface and corrected parameter types
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

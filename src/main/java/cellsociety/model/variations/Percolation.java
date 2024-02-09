package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.BasicCell;
import java.util.List;

public class Percolation implements Simulation<BasicCell> {

  private static final String PERCOLATED = CellStates.PERCOLATED.name();
  private static final String EMPTY = CellStates.EMPTY.name();

  @Override
  public BasicCell createVariationCell(int row, int col, String state) {
    return new BasicCell(row, col, state);
  }

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

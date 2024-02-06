package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.PercolationCell;
import java.util.List;

public class Percolation implements Simulation<PercolationCell> {

  private final String PERCOLATED = CellStates.PERCOLATED.name();
  private final String EMPTY = CellStates.EMPTY.name();
  private final String WALL = CellStates.WALL.name();

  @Override
  public PercolationCell createVariationCell(int row, int col, String state) {
    return new PercolationCell(row, col, state);
  }

  @Override
  public String determineState(PercolationCell cell, String currentState, List<PercolationCell> neighbors) {

    // If water found
    if (currentState.equals(PERCOLATED)) {

    }

    return currentState; // cell's state never changes
  }
}

package cellsociety.model.Variations;

import cellsociety.model.Cell;
import cellsociety.model.Simulation;
import cellsociety.model.VariationCells.WaTorCells;
import java.util.List;

public class WaTor implements Simulation {

  @Override
  public Cell createVariationCell(int row, int col, String state) {
    return new WaTorCells(row, col, state);
  }

  @Override
  public String determineState(Cell cell, String currentState, List<Cell> neighbors) {
    return "PLACEHOLDER";
  }
}

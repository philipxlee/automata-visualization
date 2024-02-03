package cellsociety.model.Variations;

import cellsociety.model.Cell;
import cellsociety.model.Simulation;
import cellsociety.model.VariationCells.WaTorCells;
import java.util.List;

public class WaTor implements Simulation {

  /**
   * Creates a new WaTorCell with specified row, column, and state.
   * This method is specific to the WaTor simulation, where cells can represent fish, sharks, or water.
   * @param row The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
   * @param state The initial state of the cell, representing fish, sharks, or water.
   * @return A new instance of WaTorCell with the given parameters.
   */
  @Override
  public Cell createVariationCell(int row, int col, String state) {
    return new WaTorCells(row, col, state);
  }


  @Override
  public String determineState(Cell cell, String currentState, List<Cell> neighbors) {






    return "PLACEHOLDER";
  }
}

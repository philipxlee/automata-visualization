package cellsociety.model.celltypes;

import cellsociety.model.Cell;

public class BasicCell extends Cell {

  /**
   * Create a new cell with the given state at the given row and column
   *
   * @param row
   * @param col
   * @param state
   */
  public BasicCell(int row, int col, String state) {
    super(row, col, state);
  }
}

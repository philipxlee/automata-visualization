package cellsociety.model.celltypes;

import cellsociety.model.Cell;

public class SandCell extends Cell {

  private boolean stacked = false;

  /**
   * Create a new cell with the given state at the given row and column
   *
   * @param row
   * @param col
   * @param state
   */
  public SandCell(int row, int col, String state) {
    super(row, col, state);
  }

  public boolean getStacked() {
    return this.stacked;
  }

  public void setStacked(boolean stacked) {
    this.stacked = stacked;
  }

}

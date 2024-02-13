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

  /**
   * Returns whether the sand cell is stacked (i.e. no longer falling)
   *
   * @return whether the sand cell is stacked
   */
  public boolean getStacked() {
    return this.stacked;
  }

  /**
   * Sets whether the sand cell is stacked (i.e. no longer falling)
   *
   * @param stacked whether the sand cell is stacked
   */
  public void setStacked(boolean stacked) {
    this.stacked = stacked;
  }

}

package cellsociety.model.edgepolicy;

import cellsociety.model.Cell;

public class NormalEdgePolicy<T extends Cell> extends AbstractEdgePolicy<T> {

  /**
   * Check if the new row and column are valid neighbors
   *
   * @param row      The row of the cell
   * @param col      The column of the cell
   * @param newRow   The new row
   * @param newCol   The new column
   * @param cellGrid The cell grid
   * @return true if the new row and column are valid neighbors, false otherwise
   */
  @Override
  public boolean isValidNeighbor(int row, int col, int newRow, int newCol, T[][] cellGrid) {
    return true;
  }
}

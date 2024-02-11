package cellsociety.model.edgepolicy;

import cellsociety.model.Cell;
import java.util.ArrayList;
import java.util.List;

public class VerticalEdgePolicy<T extends Cell> implements EdgePolicy<T> {

  /**
   * Returns a list of the neighbors of the cell at the given row and column in the grid.
   * Implements the vertical edge policy, where the grid an "edge" is creating that splits
   * the grid in halves. The neighbors of a cell are the cells in the same half of the grid.
   *
   * @param row The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
   * @param cellGrid The grid of cells in the simulation.
   * @return A list of the cell's neighbors.
   */
  @Override
  public List<T> getNeighbors(int row, int col, T[][] cellGrid) {
    List<T> neighbors = new ArrayList<>();
    int[][] directions = {{-1, 0}, {-1, -1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, 1}};
    for (int[] direction : directions) {
      int newRow = row + direction[0];
      int newCol = col + direction[1];
      int m = cellGrid.length;
      int n = cellGrid[0].length;
      if (isWithinBounds(newRow, newCol, m, n) && isValidNeighbor(col, newCol, n)) {
        neighbors.add(cellGrid[newRow][newCol]);
      }
    }
    return neighbors;
  }

  private boolean isWithinBounds(int newRow, int newCol, int totalRows, int totalCols) {
    return newRow >= 0 && newRow < totalRows && newCol >= 0 && newCol < totalCols;
  }

  private boolean isValidNeighbor(int col, int newCol, int totalCols) {
    int splitPoint = totalCols / 2;
    return !(col < splitPoint && newCol >= splitPoint || col >= splitPoint && newCol < splitPoint);
  }

}

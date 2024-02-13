package cellsociety.model.edgepolicy;

import cellsociety.model.Cell;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEdgePolicy<T extends Cell> implements EdgePolicy<T> {

  /**
   * Get the neighbors of a cell at the given row and column in the given grid
   *
   * @param row  The row of the cell
   * @param col  The column of the cell
   * @param grid The grid
   * @return the neighbors of the cell at the given row and column in the given grid
   */
  public List<T> getNeighbors(int row, int col, T[][] grid) {
    List<T> neighbors = new ArrayList<>();
    int[][] directions = {{-1, 0}, {-1, -1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, 1}};
    for (int[] direction : directions) {
      int newRow = row + direction[0];
      int newCol = col + direction[1];
      if (isInBounds(newRow, newCol, grid) && isValidNeighbor(row, col, newRow, newCol, grid)) {
        neighbors.add(grid[newRow][newCol]);
      }
    }
    return neighbors;
  }

  /**
   * Check if the new row and column are in bounds of the grid
   *
   * @param newRow The new row
   * @param newCol The new column
   * @param grid   The grid
   * @return true if the new row and column are in bounds of the grid, false otherwise
   */
  public boolean isInBounds(int newRow, int newCol, T[][] grid) {
    return newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length;
  }

  /**
   * Check if the new row and column are valid neighbors
   *
   * @param row    The row
   * @param col    The column
   * @param newRow The new row
   * @param newCol The new column
   * @param grid   The grid
   * @return true if the new row and column are valid neighbors, false otherwise
   */
  public abstract boolean isValidNeighbor(int row, int col, int newRow, int newCol, T[][] grid);
}

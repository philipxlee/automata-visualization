package cellsociety.model.edgepolicy;

import cellsociety.model.Cell;
import java.util.ArrayList;
import java.util.List;

public class NormalEdgePolicy<T extends Cell> implements EdgePolicy<T> {

  /**
   * Returns a list of the neighbors of the cell at the given row and column in the grid.
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
      if (newRow >= 0 && newRow < cellGrid.length && newCol >= 0 && newCol < cellGrid[0].length) {
        neighbors.add(cellGrid[newRow][newCol]);
      }
    }
    return neighbors;
  }
}

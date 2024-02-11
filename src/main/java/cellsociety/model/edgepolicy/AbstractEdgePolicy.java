package cellsociety.model.edgepolicy;

import cellsociety.model.Cell;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEdgePolicy<T extends Cell> implements EdgePolicy<T> {

  protected List<T> getCommonNeighbors(int row, int col, T[][] cellGrid, boolean applyEdgePolicy) {
    List<T> neighbors = new ArrayList<>();
    int[][] directions = {{-1, 0}, {-1, -1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, 1}};
    for (int[] direction : directions) {
      int newRow = row + direction[0];
      int newCol = col + direction[1];
      int m = cellGrid.length;
      int n = cellGrid[0].length;
      if (isWithinBounds(newRow, newCol, m, n) && (!applyEdgePolicy || isValidNeighbor(col, newCol, n))) {
        neighbors.add(cellGrid[newRow][newCol]);
      }
    }
    return neighbors;
  }

  private boolean isWithinBounds(int newRow, int newCol, int totalRows, int totalCols) {
    return newRow >= 0 && newRow < totalRows && newCol >= 0 && newCol < totalCols;
  }

  protected abstract boolean isValidNeighbor(int col, int newCol, int totalCols);
}

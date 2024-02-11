package cellsociety.model.edgepolicy;

import cellsociety.model.Cell;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEdgePolicy<T extends Cell> implements EdgePolicy<T> {

  public List<T> getNeighbors(int row, int col, T[][] cellGrid) {
    List<T> neighbors = new ArrayList<>();
    int[][] directions = {{-1, 0}, {-1, -1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, 1}};
    for (int[] direction : directions) {
      int newRow = row + direction[0];
      int newCol = col + direction[1];
      if (isInBounds(newRow, newCol, cellGrid) && isValidNeighbor(row, col, newRow, newCol, cellGrid)) {
        neighbors.add(cellGrid[newRow][newCol]);
      }
    }
    return neighbors;
  }

  public boolean isInBounds(int newRow, int newCol, T[][] cellGrid) {
    return newRow >= 0 && newRow < cellGrid.length && newCol >= 0 && newCol < cellGrid[0].length;
  }

  public abstract boolean isValidNeighbor(int row, int col, int newRow, int newCol, T[][] cellGrid);
}

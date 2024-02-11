package cellsociety.model.edgepolicy;

import cellsociety.model.Cell;
import java.util.List;

public class VerticalEdgePolicy<T extends Cell> extends AbstractEdgePolicy<T> {

  @Override
  public List<T> getNeighbors(int row, int col, T[][] cellGrid) {
    return getCommonNeighbors(row, col, cellGrid, true); // Apply vertical split edge policy
  }

  @Override
  protected boolean isValidNeighbor(int col, int newCol, int totalCols) {
    int splitPoint = totalCols / 2;
    return !(col < splitPoint && newCol >= splitPoint || col >= splitPoint && newCol < splitPoint);
  }
}
package cellsociety.model.edgepolicy;

import cellsociety.model.Cell;
import java.util.ArrayList;
import java.util.List;

public class NormalEdgePolicy<T extends Cell> extends AbstractEdgePolicy<T> {

  @Override
  public List<T> getNeighbors(int row, int col, T[][] cellGrid) {
    return getCommonNeighbors(row, col, cellGrid, false); // No special edge policy
  }

  @Override
  protected boolean isValidNeighbor(int col, int newCol, int totalCols) {
    return true;
  }
}

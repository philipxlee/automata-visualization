package cellsociety.model.edgepolicy;

import cellsociety.model.Cell;
import java.util.List;

public interface EdgePolicy<T extends Cell> {

  List<T> getNeighbors(int row, int col, T[][] cellGrid);

}

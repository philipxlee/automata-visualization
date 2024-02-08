package cellsociety.model;

import java.util.List;

public interface Simulation<CellType extends Cell> {

  CellType createVariationCell(int row, int col, String state);

  void prepareCellNextState(CellType cell, List<CellType> neighbors);
}

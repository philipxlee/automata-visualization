package cellsociety.model;

import java.util.List;


public interface Simulation<CellType extends Cell> {

  CellType createVariationCell(int row, int col, String state);

  String determineState(CellType cell, String currentState, List<CellType> neighbors);

}

package cellsociety.model;

import java.util.List;


public interface Simulation {

  Cell createVariationCell(int row, int col, String state);
  String determineState(Cell cell, String currentState, List<Cell> neighbors);

}



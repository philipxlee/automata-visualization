package cellsociety.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Simulation<CellType extends Cell> {

  Map<String, Double> parameters = new HashMap<>();

  CellType createVariationCell(int row, int col, String state);

  void prepareCellNextState(CellType cell, List<CellType> neighbors);

  default void setParameters(Map<String, Double> newParameters) {
    parameters.clear(); // Clear the current map
    parameters.putAll(newParameters);
    System.out.println(parameters.get("parameterP"));
  }
}

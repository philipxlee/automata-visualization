package cellsociety.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Simulation<Celltype extends Cell> {

  Map<String, Double> parameters = new HashMap<>();

  Celltype createVariationCell(int row, int col, String state);

  void prepareCellNextState(Celltype cell, List<Celltype> neighbors);

  default void setParameters(Map<String, Double> newParameters) {
    parameters.clear(); // Clear the current map
    parameters.putAll(newParameters);
    System.out.println(parameters.get("parameterP"));
  }
}

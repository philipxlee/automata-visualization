package cellsociety.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Simulation<T extends Cell> {

  Map<String, Double> parameters = new HashMap<>();

  T createVariationCell(int row, int col, String state);

  void prepareCellNextState(T cell, List<T> neighbors);

  default void setParameters(Map<String, Double> newParameters) {
    parameters.clear(); // Clear the current map
    parameters.putAll(newParameters);
  }
}

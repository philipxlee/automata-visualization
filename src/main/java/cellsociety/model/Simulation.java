package cellsociety.model;

import java.util.List;


public interface Simulation {

  String determineState(Cell cell, String currentState, List<Cell> neighbors);

}



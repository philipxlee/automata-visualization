package cellsociety.model;

import java.util.List;

public abstract class Simulation {

  public abstract String determineState(Cell cell, String currentState, List<Cell> neighbors);

  protected int countAliveNeighbors(List<Cell> neighbors) {
    int aliveNeighbors = 0;
    for (Cell neighbor : neighbors) {
      int alive = neighbor.getState().equals("ALIVE") ? 1 : 0;
      aliveNeighbors += alive;
    }
    return aliveNeighbors;
  }
}

class GameOfLife extends Simulation {
  @Override
  public String determineState(Cell cell, String currentState, List<Cell> neighbors) {
    int aliveNeighbors = countAliveNeighbors(neighbors);
    switch (currentState) {
      case "ALIVE" -> currentState = (aliveNeighbors < 2 || aliveNeighbors > 3) ? "DEAD" : "ALIVE";
      case "DEAD" -> currentState = (aliveNeighbors == 3) ? "ALIVE" : "DEAD";
      default -> throw new IllegalStateException("Unexpected cell state: " + currentState);
    }
    return currentState;
  }
}

class SpreadingOfFire extends Simulation {
  @Override
  public String determineState(Cell cell, String currentState, List<Cell> neighbors) {
    int aliveNeighbors = countAliveNeighbors(neighbors);



    return currentState;
  }
}

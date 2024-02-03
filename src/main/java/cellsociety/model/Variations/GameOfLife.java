package cellsociety.model.Variations;

import cellsociety.model.Cell;
import cellsociety.model.Simulation;
import cellsociety.model.VariationCells.GameOfLifeCell;
import java.util.List;

public class GameOfLife implements Simulation {

  /**
   * Creates a new GameOfLifeCell with specified row, column, and state.
   * @param row The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
   * @param state The initial state of the cell, usually "ALIVE" or "DEAD".
   * @return A new instance of GameOfLifeCell with the given parameters.
   */
  @Override
  public Cell createVariationCell(int row, int col, String state) {
    return new GameOfLifeCell(row, col, state);
  }

  /**
   * Determines the next state of a cell based on its current state and the states of its neighbors.
   * This method implements the rules of Conway's Game of Life:
   * - An "ALIVE" cell with fewer than two or more than three alive neighbors becomes "DEAD".
   * - An "ALIVE" cell with exactly two or three alive neighbors stays "ALIVE".
   * - A "DEAD" cell with exactly three alive neighbors becomes "ALIVE".
   * If the cell's current state does not match "ALIVE" or "DEAD", an IllegalStateException is thrown.
   * @param cell The cell whose next state is to be determined.
   * @param currentState The current state of the cell, typically "ALIVE" or "DEAD".
   * @param neighbors A list of the cell's neighbors, used to count how many are in the "ALIVE" state.
   * @return The next state of the cell ("ALIVE" or "DEAD") based on the rules of the simulation.
   * @throws IllegalStateException if the cell's current state is neither "ALIVE" nor "DEAD".
   */
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

  private int countAliveNeighbors(List<Cell> neighbors) {
    int aliveNeighbors = 0;
    for (Cell neighbor : neighbors) {
      if(neighbor == null) continue; //maybe change
      int alive = neighbor.getState().equals("ALIVE") ? 1 : 0;
      aliveNeighbors += alive;
    }
    return aliveNeighbors;
  }
}

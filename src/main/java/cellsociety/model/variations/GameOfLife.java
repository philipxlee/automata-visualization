package cellsociety.model.variations;

import static jdk.internal.joptsimple.internal.Strings.EMPTY;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.BasicCell;
import java.util.List;

public class GameOfLife implements Simulation<BasicCell> {

  private static final String ALIVE = CellStates.ALIVE.name();
  private static final String DEAD = CellStates.DEAD.name();

  /**
   * Creates a new GameOfLifeCell with specified row, column, and state.
   *
   * @param row   The row position of the cell in the grid.
   * @param col   The column position of the cell in the grid.
   * @param state The initial state of the cell, usually "ALIVE" or "DEAD".
   * @return A new instance of GameOfLifeCell with the given parameters.
   */
  @Override
  public BasicCell createVariationCell(int row, int col, String state) {
    return new BasicCell(row, col, state);
  }

  /**
   * Determines the next state of a cell based on its current state and the states of its neighbors.
   * This method implements the rules of Conway's Game of Life: - An "ALIVE" cell with fewer than
   * two or more than three alive neighbors becomes "DEAD". - An "ALIVE" cell with exactly two or
   * three alive neighbors stays "ALIVE". - A "DEAD" cell with exactly three alive neighbors becomes
   * "ALIVE". If the cell's current state does not match "ALIVE" or "DEAD", an IllegalStateException
   * is thrown.
   *
   * @param cell      The cell whose next state is to be determined.
   * @param neighbors A list of the cell's neighbors, used to count how many are "ALIVE"
   * @throws IllegalStateException if the cell's current state is neither "ALIVE" nor "DEAD".
   */
  @Override
  public void prepareCellNextState(BasicCell cell, List<BasicCell> neighbors) {
    int aliveNeighbors = countAliveNeighbors(neighbors);
    String nextState;
    switch (cell.getState()) {
      case "ALIVE" -> nextState = (aliveNeighbors < 2 || aliveNeighbors > 3) ? DEAD : ALIVE;
      case "DEAD" -> nextState = (aliveNeighbors == 3) ? ALIVE : DEAD;
      case "EMPTY" -> nextState = EMPTY;
      default -> throw new IllegalStateException("Unexpected cell state: " + cell.getState());
    }
    cell.setNextState(nextState); // Assuming BasicCell has a method setNextState
  }

  private int countAliveNeighbors(List<BasicCell> neighbors) {
    int aliveNeighbors = 0;
    for (BasicCell neighbor : neighbors) {
      int alive = neighbor.getState().equals(ALIVE) ? 1 : 0;
      aliveNeighbors += alive;
    }
    return aliveNeighbors;
  }
}

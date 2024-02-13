package cellsociety.model.variations;

import cellsociety.model.Simulation;
import cellsociety.model.celltypes.ants.AntsCell;
import java.util.List;

public class ForagingAnts implements Simulation<AntsCell> {

  /**
   * Creates a new AntsCell with specified row, column, and state.
   *
   * @param row   The row position of the cell in the grid.
   * @param col   The column position of the cell in the grid.
   * @param state The initial state of the cell, usually "HOME", "FOOD", "ANT", or "EMPTY".
   * @return A new instance of WaTorCell with the given parameters.
   */
  @Override
  public AntsCell createVariationCell(int row, int col, String state) {
    return new AntsCell(row, col, state);
  }

  /**
   * Determines the next state of a cell based on its current state and the states of its neighbors.
   * This method implements the rules of Foraging Ants simulation: - An "ANT" cell moves to a
   * neighboring cell with the highest pheromone level. - An "ANT" cell drops pheromone on its path
   * to food or home. - An "ANT" cell picks up food and returns to the home cell. - A "HOME" cell
   * evaporates food pheromone and diffuses food pheromone. - A "FOOD" cell evaporates home
   * pheromone and diffuses home pheromone. - A "HOME" cell evaporates home pheromone and diffuses
   * home pheromone. - A "FOOD" cell evaporates food pheromone and diffuses food pheromone.
   *
   * @param cell      The cell whose next state is to be determined.
   * @param neighbors A list of the cell's neighbors, used to count how many are "ALIVE"
   */
  @Override
  public void prepareCellNextState(AntsCell cell, List<AntsCell> neighbors) {
    double evaporationRate = 0.001;
    cell.evaporateFoodPheromone(evaporationRate);
    cell.evaporateHomePheromone(evaporationRate);
    double diffusionRate = 0.001;
    cell.diffuseFoodPheromone(diffusionRate, neighbors);
    cell.diffuseHomePheromone(diffusionRate, neighbors);
    cell.setState(cell.updateCellState());
    cell.antsForage(neighbors);
    cell.birthAnts();
  }
}

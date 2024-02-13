package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.AntsCell;
import cellsociety.model.celltypes.AntsCell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForagingAnts implements Simulation<AntsCell> {

  private static final String HOME = CellStates.HOME.name();
  private static final String FOOD = CellStates.FOOD.name();
  private static final String ANT = CellStates.ANT.name();
  private static final String EMPTY = CellStates.EMPTY.name();
  private static final String HIGHPHEROMONE = CellStates.HIGHPHEROMONE.name();
  private static final String LOWPHEROMONE = CellStates.LOWPHEROMONE.name();
  private double diffusionRate = 0.0005;
  private double evaporationRate = 0.02;

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

  @Override
  public void prepareCellNextState(AntsCell cell, List<AntsCell> neighbors) {
    cell.evaporateFoodPheromone(evaporationRate);
    cell.evaporateHomePheromone(evaporationRate);
    cell.diffuseFoodPheromone(diffusionRate, neighbors);
    cell.diffuseHomePheromone(diffusionRate, neighbors);
    cell.setState(cell.updateCellState());
    cell.antsForage(neighbors);
    cell.birthAnts();
  }

}

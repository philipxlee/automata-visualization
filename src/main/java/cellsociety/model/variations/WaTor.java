package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.WaTorCell;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class WaTor implements Simulation<WaTorCell> {

  private static final String FISH = CellStates.FISH.name();
  private static final String SHARK = CellStates.SHARK.name();
  private static final String EMPTY = CellStates.EMPTY.name();
  private Random rand = new Random();

  /**
   * Creates a new WaTorCell with specified row, column, and state.
   *
   * @param row The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
   * @param state The initial state of the cell, usually "FISH", "SHARK", or "EMPTY".
   * @return A new instance of WaTorCell with the given parameters.
   */
  @Override
  public WaTorCell createVariationCell(int row, int col, String state) {
    return new WaTorCell(row, col, state);
  }

  /**
   * Determines the next state of a cell based on its current state and states of its neighbors.
   * This method implements the rules of WaTor: - A "FISH" cell moves to an empty cell if there is
   * one available. - A "SHARK" cell moves to a cell with a fish in it if there is one available.
   * - A "SHARK" cell moves to an empty cell if there are no fish available. - A "SHARK" cell dies
   * if it is not eaten in a certain number of time steps. - A "FISH" cell reproduces if it has not
   * reproduced in a certain number of time steps. - A "SHARK" cell reproduces if it has not
   * reproduced in a certain number of time steps. - A "SHARK" cell eats a fish if it moves to a
   * cell with a fish in it.
   *
   * @param cell The cell whose next state is to be determined.
   * @param neighbors A list of the cell's neighbors, used to check if any are "EMPTY" or "WALL"
   */
  @Override
  public void prepareCellNextState(WaTorCell cell, List<WaTorCell> neighbors) {
    if (cell.getState().equals(FISH)) {
      moveFish(cell, neighbors);
    } else if (cell.getState().equals(SHARK)) {
      moveShark(cell, neighbors);
    }
  }

  private void moveFish(WaTorCell cell, List<WaTorCell> neighbors) {
    List<WaTorCell> availableSpaces = getAvailableSpaces(neighbors);
    if (!availableSpaces.isEmpty()) {
      WaTorCell newSpace = availableSpaces.get(rand.nextInt(availableSpaces.size()));
      reproduceOrMove(cell, newSpace, FISH);
    }
  }

  private void moveShark(WaTorCell cell, List<WaTorCell> neighbors) {
    List<WaTorCell> fishSpaces = getFishSpaces(neighbors);
    if (!fishSpaces.isEmpty()) {
      WaTorCell fishSpace = fishSpaces.get(rand.nextInt(fishSpaces.size()));
      cell.eatFish(fishSpace);
      reproduceOrMove(cell, fishSpace, SHARK);
    } else {
      List<WaTorCell> availableSpaces = getAvailableSpaces(neighbors);
      if (!availableSpaces.isEmpty()) {
        WaTorCell newSpace = availableSpaces.get(rand.nextInt(availableSpaces.size()));
        reproduceOrMove(cell, newSpace, SHARK);
      }
      cell.decreaseEnergy();
      if (cell.sharkStarve()) {
        cell.die();
      }
    }
  }

  private List<WaTorCell> getAvailableSpaces(List<WaTorCell> neighbors) {
    List<WaTorCell> available = new ArrayList<>();
    for (WaTorCell neighbor : neighbors) {
      if (neighbor.getState().equals(EMPTY)) {
        available.add(neighbor);
      }
    }
    return available;
  }

  private List<WaTorCell> getFishSpaces(List<WaTorCell> neighbors) {
    List<WaTorCell> fish = new ArrayList<>();
    for (WaTorCell neighbor : neighbors) {
      if (neighbor.getState().equals(FISH)) {
        fish.add(neighbor);
      }
    }
    return fish;
  }

  private void reproduceOrMove(WaTorCell cell, WaTorCell newSpace, String type) {
    if (cell.canReproduce(type)) {
      cell.resetBreedTime();
      newSpace.become(type);
      if (type.equals(SHARK)) {
        newSpace.setEnergy(WaTorCell.SHARK_STARTING_ENERGY);
      }
      cell.become(new WaTorCell(cell.getRow(), cell.getCol(), type).getState()); // Reproduce
    } else {
      cell.increaseBreedTime();
      newSpace.become(type);
      if (type.equals(SHARK)) {
        newSpace.setEnergy(cell.getEnergy());
      }
      cell.become(EMPTY);
    }
  }
}

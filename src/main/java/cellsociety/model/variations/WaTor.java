package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.WaTorCell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaTor implements Simulation<WaTorCell> {

  private static final String FISH = CellStates.FISH.name();
  private static final String SHARK = CellStates.SHARK.name();
  private static final String EMPTY = CellStates.EMPTY.name();
  private static final Random rand = new Random();

  /**
   * Creates a new WaTorCell with specified row, column, and state.
   *
   * @param row  The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
   * @param state The initial state of the cell, usually "FISH", "SHARK", or "EMPTY".
   * @return A new instance of WaTorCell with the given parameters.
   */
  @Override
  public WaTorCell createVariationCell(int row, int col, String state) {
    return new WaTorCell(row, col, state);
  }

  /**
   * Determines the next state of a cell based on its current state and the states of its neighbors.
   * This method implements the rules of WaTor: - A "FISH" cell moves to an empty cell if there is
   * one available. - A "SHARK" cell moves to an empty cell if there is one available, or to a cell
   * with a fish if there is one available. - A "SHARK" cell dies if it has not eaten in a certain
   * number of time steps. - A "SHARK" cell reproduces if it has not reproduced in a certain number
   * of time steps. - A "FISH" cell reproduces if it has not reproduced in a certain number of time
   * steps.
   *
   * @param cell The cell whose next state is to be determined.
   * @param neighbors A list of the cell's neighbors, used to check if any are "FISH" or "SHARK"
   */
  @Override
  public void prepareCellNextState(WaTorCell cell, List<WaTorCell> neighbors) {
    String currentState = cell.getState();
    switch (currentState) {
      case "FISH" -> handleFish(cell, neighbors);
      case "SHARK" -> handleShark(cell, neighbors);
      default -> cell.setNextState(EMPTY);
    }
  }

  private void handleFish(WaTorCell currentCell, List<WaTorCell> neighbors) {
    List<WaTorCell> emptyNeighbors = findNeighbors(neighbors, EMPTY);
    if (!emptyNeighbors.isEmpty()) {
      WaTorCell nextCell = emptyNeighbors.get(rand.nextInt(emptyNeighbors.size()));
      moveFish(currentCell, nextCell);
    }
  }

  private void moveFish(WaTorCell currentCell, WaTorCell nextCell) {
    if (currentCell.getIsEaten()) {
      currentCell.setNextState(SHARK);
      currentCell.resetAnimal();
      return;
    }
    currentCell.setNextState(EMPTY);
    nextCell.setNextState(FISH);
    nextCell.setBreedTime(currentCell.getBreedTime() + 1);
    if (nextCell.canReproduce(FISH)) {
      currentCell.setNextState(FISH);
      currentCell.resetAnimal();
      nextCell.resetAnimal();
    } else {
      currentCell.setNextState(EMPTY);
      currentCell.resetAnimal();
    }
  }

  private void handleShark(WaTorCell currentCell, List<WaTorCell> neighbors) {
    List<WaTorCell> fishNeighbors = findNeighbors(neighbors, FISH);
    if (!fishNeighbors.isEmpty()) {
      WaTorCell nextCell = fishNeighbors.get(rand.nextInt(fishNeighbors.size()));
      moveShark(currentCell, nextCell);
      return;
    }

    List<WaTorCell> emptyNeighbors = findNeighbors(neighbors, EMPTY);
    if (!emptyNeighbors.isEmpty()) {
      WaTorCell nextCell = emptyNeighbors.get(rand.nextInt(emptyNeighbors.size()));
      moveShark(currentCell, nextCell);
      return;
    }

    currentCell.setNextState(SHARK);
  }

  private void moveShark(WaTorCell currentCell, WaTorCell nextCell) {
    nextCell.setNextState(SHARK);
    nextCell.setBreedTime(currentCell.getBreedTime() + 1);

    if (nextCell.getState().equals(FISH)) {
      nextCell.setEnergy(currentCell.getEnergy() + 2);
      nextCell.setIsEaten(true);
    } else {
      nextCell.setEnergy(currentCell.getEnergy() - 1);
    }

    if (nextCell.sharkStarve()) {
      currentCell.setNextState(EMPTY);
      currentCell.resetAnimal();
      nextCell.setNextState(EMPTY);
      nextCell.resetAnimal();
    } else if (nextCell.canReproduce(SHARK)) {
      currentCell.setNextState(SHARK);
      currentCell.resetAnimal();
      nextCell.setBreedTime(0);
    } else {
      currentCell.setNextState(EMPTY);
      currentCell.resetAnimal();
    }
  }

  private List<WaTorCell> findNeighbors(List<WaTorCell> neighbors, String target) {
    List<WaTorCell> validNeighbors = new ArrayList<>();
    for (WaTorCell neighbor : neighbors) {
      if (neighbor.getState().equals(target)) {
        validNeighbors.add(neighbor);
      }
    }
    return validNeighbors;
  }
}
package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.WaTorCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WaTor implements Simulation<WaTorCell> {

  private static final String FISH = CellStates.FISH.name();
  private static final String SHARK = CellStates.SHARK.name();
  private static final String EMPTY = CellStates.EMPTY.name();
  private static final Random rand = new Random();

  @Override
  public WaTorCell createVariationCell(int row, int col, String state) {
    return new WaTorCell(row, col, state);
  }

  @Override
  public void prepareCellNextState(WaTorCell cell, List<WaTorCell> neighbors) {
    String currentState = cell.getState();
    switch (currentState) {
      case "FISH" -> handleFish(cell, neighbors);
      case "SHARK" -> handleShark(cell, neighbors);
    }
  }

  private void handleFish(WaTorCell currentCell, List<WaTorCell> neighbors) {
    List<WaTorCell> emptyNeighbors = findNeighbors(currentCell, neighbors, EMPTY);
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
    currentCell.setFishMovedAway(true);
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
    List<WaTorCell> fishNeighbors = findNeighbors(currentCell, neighbors, FISH);
    if (!fishNeighbors.isEmpty()) {
      WaTorCell nextCell = fishNeighbors.get(rand.nextInt(fishNeighbors.size()));
      moveShark(currentCell, nextCell);
      return;
    }

    List<WaTorCell> emptyNeighbors = findNeighbors(currentCell, neighbors, EMPTY);
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
    }
    else if (nextCell.canReproduce(SHARK)) {
      currentCell.setNextState(SHARK);
      currentCell.resetAnimal();
      nextCell.setBreedTime(0);
    } else {
      currentCell.setNextState(EMPTY);
      currentCell.resetAnimal();
    }
  }

  private List<WaTorCell> findNeighbors(WaTorCell cell, List<WaTorCell> neighbors, String target) {
    List<WaTorCell> validNeighbors = new ArrayList<>();
    for (WaTorCell neighbor : neighbors) {
      if (neighbor.getState().equals(target)) {
        validNeighbors.add(neighbor);
      }
    }
    return validNeighbors;
  }
}
package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.WaTorCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WaTor implements Simulation<WaTorCell> {

  private final String EMPTY = CellStates.EMPTY.name();
  private final String FISH = CellStates.FISH.name();
  private final String SHARK = CellStates.SHARK.name();

  private int fishBreedTime = 0;
  private int sharkBreedTime = 0;
  private int sharkStarveTime = 0;

  @Override
  public WaTorCell createVariationCell(int row, int col, String state) {
    return new WaTorCell(row, col, state);
  }

  @Override
  public void prepareCellNextState(WaTorCell cell, List<WaTorCell> neighbors) {
    if (cell.getState().equals(EMPTY)) {
      handleEmptyCell(cell, neighbors);
    } else if (cell.getState().equals(FISH)) {
      handleFishCell(cell, neighbors);
    } else if (cell.getState().equals(SHARK)) {
      handleSharkCell(cell, neighbors);
    }
  }

  private void handleEmptyCell(WaTorCell cell, List<WaTorCell> neighbors) {
    // Do nothing, as empty cells remain empty
  }

  private void handleFishCell(WaTorCell cell, List<WaTorCell> neighbors) {
    WaTorCell randomNeighbor = getRandomNeighbor(cell, neighbors);
    if (randomNeighbor != null && randomNeighbor.getState().equals(EMPTY)) {
      randomNeighbor.setNextState(FISH);
      randomNeighbor.setFishBreedTime(cell.getFishBreedTime() - 1);
    }
    cell.setNextState(FISH);
    cell.setFishBreedTime(fishBreedTime - 1);
  }

  private void handleSharkCell(WaTorCell cell, List<WaTorCell> neighbors) {
    WaTorCell randomNeighbor = getRandomNeighbor(cell, neighbors);
    if (randomNeighbor != null && randomNeighbor.getState().equals(EMPTY)) {
      randomNeighbor.setNextState(SHARK);
      randomNeighbor.setFishBreedTime(cell.getSharkBreedTime() - 1);
      randomNeighbor.setSharkStarveTime(cell.getSharkStarveTime() - 1);
    } else if (randomNeighbor != null && randomNeighbor.getState().equals(FISH)) {
      randomNeighbor.setNextState(SHARK);
      randomNeighbor.setFishBreedTime(cell.getSharkBreedTime() - 1);
      randomNeighbor.setSharkStarveTime(cell.getSharkStarveTime() - 1);
    }
    cell.setNextState(EMPTY);
  }

  private WaTorCell getRandomNeighbor(WaTorCell cell, List<WaTorCell> neighbors) {
    List<WaTorCell> emptyNeighbors = new ArrayList<>();
    for (WaTorCell neighbor : neighbors) {
      if (neighbor.getState().equals(EMPTY)) {
        emptyNeighbors.add(neighbor);
      }
    }
    if (!emptyNeighbors.isEmpty()) {
      Collections.shuffle(emptyNeighbors);
      return emptyNeighbors.get(0);
    }
    return null;
  }
}
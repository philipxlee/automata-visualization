package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.WaTorCell;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WaTor implements Simulation<WaTorCell> {

  private final String EMPTY = CellStates.EMPTY.name();
  private final String FISH = CellStates.FISH.name();
  private final String SHARK = CellStates.SHARK.name();
  private Set<String> claimedCells = new HashSet<>();

  private Random rand = new Random();

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

  private void handleFish(WaTorCell fish, List<WaTorCell> neighbors) {
    List<WaTorCell> emptySpaces = findSpecificNeighbors(neighbors, EMPTY);
    emptySpaces.removeIf(e -> claimedCells.contains(e.getRow() + "," + e.getCol()));
    if (!emptySpaces.isEmpty()) {
      WaTorCell target = emptySpaces.get(rand.nextInt(emptySpaces.size()));
      String fishState = fish.getState();
      int fishReproductionTime = fish.getReproductionTime();
      int fishEnergy = fish.getEnergy();

      // move fishes
      boolean reproduced = moveAnimal(FISH, target, fishState, fishEnergy, fishReproductionTime + 1);

      // check for reproduction
      if (reproduced) {
        fish.setNextState(FISH);
        fish.resetReproductionTime();
        fish.resetEnergy();;
      } else {
        fish.setNextState(EMPTY); // Move and leave the spot empty if not reproducing
        fish.resetReproductionTime();
        fish.resetEnergy();
      }
    }
  }

  private void handleShark(WaTorCell shark, List<WaTorCell> neighbors) {
    List<WaTorCell> fishCells = findSpecificNeighbors(neighbors, FISH);
    fishCells.removeIf(e -> claimedCells.contains(e.getRow() + "," + e.getCol()));
    if (!fishCells.isEmpty()) {
      WaTorCell target = fishCells.get(rand.nextInt(fishCells.size()));
      String sharkState = shark.getState();
      int sharkReproductionTime = shark.getReproductionTime();
      int sharkEnergy = shark.getEnergy();

      // move shark
      boolean reproduced = moveAnimal(SHARK, target, sharkState, sharkEnergy + 2,
          sharkReproductionTime + 1);

      if (reproduced) {
        shark.setNextState(SHARK);
        shark.resetReproductionTime();
        shark.resetEnergy();
      } else {
        shark.setNextState(EMPTY); // Move and leave the spot empty if not reproducing
        shark.resetReproductionTime();
        shark.resetEnergy();
      }
      return;
    }

    List<WaTorCell> emptySpaces = findSpecificNeighbors(neighbors, EMPTY);
    if (!emptySpaces.isEmpty()) {

      WaTorCell target = emptySpaces.get(rand.nextInt(emptySpaces.size()));
      String sharkState = shark.getState();
      int sharkReproductionTime = shark.getReproductionTime();
      int sharkEnergy = shark.getEnergy();

      // move shark
      boolean reproduced = moveAnimal(SHARK, target, sharkState, sharkEnergy - 1, sharkReproductionTime + 1);

      // check for reproduction
      if (reproduced) {
        shark.setNextState(SHARK);
        shark.resetReproductionTime();
        shark.resetEnergy();
      } else {
        shark.setNextState(EMPTY); // Move and leave the spot empty if not reproducing
        shark.resetReproductionTime();
        shark.resetEnergy();
      }
    }
  }


  private boolean moveAnimal(String animal, WaTorCell target, String state, int energy, int reproductionTime) {
    claimedCells.add(target.getRow() + "," + target.getCol());
    target.setNextState(state); // Move to the target cell
    target.setEnergy(energy); // Transfer energy
    target.setReproductionTime(reproductionTime); // Transfer reproduction time
    if (target.canReproduce(animal)) {
      target.resetReproductionTime();
      return true;
    }
    return false;
  }

  private List<WaTorCell> findSpecificNeighbors(List<WaTorCell> neighbors, String state) {
    List<WaTorCell> specificNeighbors = new ArrayList<>();
    for (WaTorCell neighbor : neighbors) {
      if (neighbor.getState().equals(state)) {
        specificNeighbors.add(neighbor);
      }
    }
    return specificNeighbors;
  }
}

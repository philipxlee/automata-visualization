package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.WaTorCell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaTor implements Simulation<WaTorCell> {

  private final String EMPTY = CellStates.EMPTY.name();
  private final String FISH = CellStates.FISH.name();
  private final String SHARK = CellStates.SHARK.name();

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
//      case "SHARK" -> handleShark(cell, neighbors);
    }
  }

  private void handleFish(WaTorCell fish, List<WaTorCell> neighbors) {
    List<WaTorCell> emptySpaces = findSpecificNeighbors(neighbors, EMPTY);

    if (!emptySpaces.isEmpty()) {

      WaTorCell target = emptySpaces.get(rand.nextInt(emptySpaces.size()));
      String fishState = fish.getState();
      int fishReproductionTime = fish.getReproductionTime();
      int fishEnergy = fish.getEnergy();

      // move fishes
      moveAnimal(target, fishState, fishEnergy, fishReproductionTime + 1);

      // check for reproduction
      if (fish.canReproduce(FISH)) {
        System.out.println("CAN REPRODUCE");
        fish.setNextState(FISH);
        fish.resetReproductionTime();
        fish.setEnergy(0);
      } else {
        fish.setNextState(EMPTY); // Move and leave the spot empty if not reproducing
        fish.resetReproductionTime();
        fish.setEnergy(0);
      }
    }
  }

  private void handleShark(WaTorCell shark, List<WaTorCell> neighbors) {
    List<WaTorCell> emptySpaces = findSpecificNeighbors(neighbors, EMPTY);
    if (!emptySpaces.isEmpty()) {

      WaTorCell target = emptySpaces.get(rand.nextInt(emptySpaces.size()));
      String originalSharkState = shark.getState();
      int originalSharkReproductionTime = shark.getReproductionTime();
      int originalSharkEnergy = shark.getEnergy();

      // move shark
      moveAnimal(target, originalSharkState, originalSharkEnergy, originalSharkReproductionTime);

      // check for reproduction
      if (shark.canReproduce(FISH)) {
        System.out.println("CAN REPRODUCE");
        shark.setNextState(FISH);
        shark.resetReproductionTime();
        shark.setEnergy(5);
      } else {
        shark.setNextState(EMPTY); // Move and leave the spot empty if not reproducing
        shark.resetReproductionTime();
        shark.setEnergy(0);
      }
      shark.setNextState(EMPTY); // Move and leave the spot empty if not reproducing
    }
  }


  private void moveAnimal(WaTorCell target, String state, int energy, int reproductionTime) {
    target.setNextState(state); // Move to the target cell
    target.setEnergy(energy); // Transfer energy
    target.setReproductionTime(reproductionTime); // Transfer reproduction time
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

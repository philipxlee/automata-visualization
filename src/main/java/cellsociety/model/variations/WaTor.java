package cellsociety.model.variations;

import cellsociety.model.Simulation;
import cellsociety.model.celltypes.WaTorCell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaTor implements Simulation<WaTorCell> {
  private Random rand = new Random();

  @Override
  public WaTorCell createVariationCell(int row, int col, String state) {
    return new WaTorCell(row, col, state);
  }

  @Override
  public void prepareCellNextState(WaTorCell cell, List<WaTorCell> neighbors) {
    if ("FISH".equals(cell.getState())) {
      prepareFish(cell, neighbors);
    } else if ("SHARK".equals(cell.getState())) {
      prepareShark(cell, neighbors);
    }
  }

  private void prepareFish(WaTorCell fish, List<WaTorCell> neighbors) {
    List<WaTorCell> emptySpaces = findEmptyNeighbors(neighbors);
    if (!emptySpaces.isEmpty()) {
      WaTorCell target = emptySpaces.get(rand.nextInt(emptySpaces.size()));
      if (fish.canReproduce("FISH")) {
        fish.setNextState("FISH"); // Leave a new fish behind
        fish.resetReproductionTime();
      } else {
        fish.setNextState("EMPTY"); // Move and leave the spot empty if not reproducing
      }
      moveAnimal(fish, target);
    }
    fish.incrementReproductionTime();
  }

  private void prepareShark(WaTorCell shark, List<WaTorCell> neighbors) {
    List<WaTorCell> fishNeighbors = findSpecificNeighbors(neighbors, "FISH");
    if (!fishNeighbors.isEmpty()) {
      WaTorCell target = fishNeighbors.get(rand.nextInt(fishNeighbors.size()));
      shark.gainEnergyFromEatingFish();
      moveAnimal(shark, target);
    } else {
      List<WaTorCell> emptySpaces = findEmptyNeighbors(neighbors);
      if (!emptySpaces.isEmpty()) {
        WaTorCell target = emptySpaces.get(rand.nextInt(emptySpaces.size()));
        moveAnimal(shark, target);
      }
      shark.decrementEnergy();
    }
    if (shark.canReproduce("SHARK")) {
      shark.setNextState("SHARK"); // Leave a new shark behind and reset reproduction time
      shark.resetReproductionTime();
    } else {
      shark.setNextState("EMPTY"); // Move and leave the spot empty if not reproducing
    }
    if (shark.isStarving()) {
      shark.setNextState("EMPTY"); // Shark dies of starvation
    }
    shark.incrementReproductionTime();
  }

  private void moveAnimal(WaTorCell animal, WaTorCell target) {
    target.setNextState(animal.getState()); // Move to the target cell
    target.setEnergy(animal.getEnergy()); // Transfer energy
    target.setReproductionTime(animal.getReproductionTime()); // Transfer reproduction time
    if (!animal.getNextState().equals("FISH") && !animal.getNextState().equals("SHARK")) {
      animal.setNextState("EMPTY"); // Only set the original spot to empty if not reproducing
    }
  }

  private List<WaTorCell> findEmptyNeighbors(List<WaTorCell> neighbors) {
    return findSpecificNeighbors(neighbors, "EMPTY");
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

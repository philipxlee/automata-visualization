package cellsociety.model.celltypes;

import cellsociety.model.Cell;

public class WaTorCell extends Cell {
  private int energy;
  private int reproductionTime;

  public WaTorCell(int row, int col, String state) {
    super(row, col, state);
    // Sharks start with initial energy; fish do not need energy
    this.energy = state.equals("SHARK") ? 5 : 0;
    this.reproductionTime = 0;
  }

  public void incrementReproductionTime() {
    reproductionTime++;
  }

  public void decrementEnergy() {
    if (getState().equals("SHARK")) {
      energy--;
    }
  }

  public boolean canReproduce(String creatureType) {
    int reproductionThreshold = creatureType.equals("FISH") ? 7 : 10;
    return reproductionTime >= reproductionThreshold;
  }

  public void resetReproductionTime() {
    reproductionTime = 0;
  }

  public void gainEnergyFromEatingFish() {
    energy += 2; // Assume eating a fish gives 2 energy units
  }

  public boolean isStarving() {
    return energy <= 0;
  }

  // Getters and Setters
  public int getEnergy() { return energy; }

  public void setEnergy(int energy) { this.energy = energy; }
  public int getReproductionTime() { return reproductionTime; }
  public void setReproductionTime(int time) { this.reproductionTime = time; }
}

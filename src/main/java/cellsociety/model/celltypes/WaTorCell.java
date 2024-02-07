package cellsociety.model.celltypes;

import cellsociety.model.Cell;

public class WaTorCell extends Cell {
  private final int STARTING_REPRODUCTION_TIME = 0;
  private final int STARTING_SHARK_ENERGY = 5;
  private final int FISH_TIME_TO_REPRODUCE = 7;
  private final int SHARK_TIME_TO_REPRODUCE = 10;

  private int energy = STARTING_SHARK_ENERGY;
  private int reproductionTime = STARTING_REPRODUCTION_TIME;

  public WaTorCell(int row, int col, String state) {
    super(row, col, state);
  }

  public boolean canReproduce(String creatureType) {
    int threshold = creatureType.equals("FISH") ? FISH_TIME_TO_REPRODUCE : SHARK_TIME_TO_REPRODUCE;
    return this.reproductionTime >= threshold;
  }

  public void resetReproductionTime() { this.reproductionTime = STARTING_REPRODUCTION_TIME; }

  public void resetEnergy() { this.energy = STARTING_SHARK_ENERGY; }

  public int getEnergy() { return this.energy; }

  public void setEnergy(int energy) { this.energy = energy; }

  public int getReproductionTime() { return this.reproductionTime; }

  public void setReproductionTime(int time) { this.reproductionTime = time; }
}

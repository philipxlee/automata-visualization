package cellsociety.model.celltypes;

import cellsociety.model.Cell;

public class WaTorCell extends Cell {

  private final int STARTING_REPRODUCTION_TIME = 0;
  private final int STARTING_ENERGY = 5;

  private int reproductionTime = STARTING_REPRODUCTION_TIME;
  private int energy = STARTING_ENERGY;

  public WaTorCell(int row, int col, String state) {
    super(row, col, state);
  }

  public int getEnergy() { return this.energy; }
  public int getReproductionTime() { return this.reproductionTime; }
  public void setEnergy(int energy) { this.energy = energy; }
  public void setReproductionTime(int reproductionTime) { this.reproductionTime = reproductionTime; }
}

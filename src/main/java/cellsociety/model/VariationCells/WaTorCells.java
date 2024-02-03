package cellsociety.model.VariationCells;

import cellsociety.model.Cell;

public class WaTorCells extends Cell {

  private int reproductionTime = 0;
  private int energy = 5;

  public WaTorCells (int row, int col, String state) {
    super(row, col, state);
  }

  public int getEnergy() { return this.energy; }
  public int getReproductionTime() { return this.reproductionTime; }
  public void setEnergy(int energy) { this.energy = energy; }
  public void setReproductionTime(int reproductionTime) { this.reproductionTime = reproductionTime; }
}

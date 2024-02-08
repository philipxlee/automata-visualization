package cellsociety.model.celltypes;

import cellsociety.model.Cell;

public class FallingSandCell extends Cell {

  private boolean isStacked = false;
  private boolean sandNeighborAbove = false;

  public FallingSandCell(int row, int col, String state) {
    super(row, col, state);
  }

  public boolean isStacked() {
    return this.isStacked;
  }

  public void setIsStacked(boolean stacked) {
    this.isStacked = stacked;
  }

  public boolean hasSandNeighborAbove() {
    return this.sandNeighborAbove;
  }

  public void setSandNeighborAbove(boolean hasNeighbor) {
    this.sandNeighborAbove = hasNeighbor;
  }
}

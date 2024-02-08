package cellsociety.model.celltypes;

import cellsociety.model.Cell;

public class WaTorCell extends Cell {


  private int fishBreedTime = 0;
  private int sharkBreedTime = 0;
  private int sharkStarveTime = 0;


  public WaTorCell(int row, int col, String state) {
    super(row, col, state);
  }

  public int getFishBreedTime() {
    return fishBreedTime;
  }

  public void setFishBreedTime(int fishBreedTime) {
    this.fishBreedTime = fishBreedTime;
  }

  public int getSharkBreedTime() {
    return sharkBreedTime;
  }

  public void setSharkBreedTime(int sharkBreedTime) {
    this.sharkBreedTime = sharkBreedTime;
  }

  public int getSharkStarveTime() {
    return sharkStarveTime;
  }

  public void setSharkStarveTime(int sharkStarveTime) {
    this.sharkStarveTime = sharkStarveTime;
  }
}

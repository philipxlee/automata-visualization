package cellsociety.model.celltypes;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;

public class WaTorCell extends Cell {

  private static final String SHARK = CellStates.SHARK.name();
  private static final int SHARK_BREED_TIME = 7;
  private static final int FISH_BREED_TIME = 5;
  private static final int SHARK_STARTING_ENERGY = 10;

  private int breedTime = 0;
  private int energy = SHARK_STARTING_ENERGY;
  private boolean eaten = false;
  private boolean fishMoved = false;

  public WaTorCell(int row, int col, String state) {
    super(row, col, state);
  }

  public int getBreedTime() {
    return this.breedTime;
  }

  public void setBreedTime(int time) {
    this.breedTime = time;
  }

  public void setEnergy(int energy) {
    this.breedTime = energy;
  }

  public int getEnergy() {
    return this.energy;
  }

  public void setFishMovedAway(boolean moved) {
    this.fishMoved = moved;
  }

  public boolean getFishMovedAway() {
    return this.fishMoved;
  }

  public boolean canReproduce(String animal) {
    return (animal.equals(SHARK)) ? breedTime == SHARK_BREED_TIME : breedTime == FISH_BREED_TIME;
  }

  public boolean sharkStarve() {
    return this.energy == 0;
  }

  public boolean getIsEaten() {
    return this.eaten;
  }

  public void setIsEaten(boolean eaten) {
    this.eaten = eaten;
  }

  public void resetAnimal() {
    this.energy = 0;
    this.breedTime = 0;
    this.eaten = false;
    this.fishMoved = false;
  }


}

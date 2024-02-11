package cellsociety.model.celltypes;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;

public class WaTorCell extends Cell {

  private static final String SHARK = CellStates.SHARK.name();
  private static final String FISH = CellStates.FISH.name();
  private static final String EMPTY = CellStates.EMPTY.name();
  public static final int SHARK_BREED_TIME = 7;
  public static final int FISH_BREED_TIME = 5;
  public static final int SHARK_STARTING_ENERGY = 10;

  private int breedTime = 0;
  private int energy = SHARK_STARTING_ENERGY;

  public WaTorCell(int row, int col, String state) {
    super(row, col, state);
    if (state.equals(SHARK)) {
      this.energy = SHARK_STARTING_ENERGY;
    }
  }

  public int getEnergy() {
    return energy;
  }

  public void setEnergy(int energy) {
    this.energy = energy;
  }

  public void eatFish(WaTorCell fishCell) {
    this.energy += 2;
  }

  public void decreaseEnergy() {
    this.energy -= 1;
  }

  public boolean sharkStarve() {
    return this.energy <= 0;
  }

  public void increaseBreedTime() {
    this.breedTime++;
  }

  public void resetBreedTime() {
    this.breedTime = 0;
  }

  public boolean canReproduce(String type) {
    if (type.equals(SHARK)) {
      return breedTime >= SHARK_BREED_TIME;
    } else if (type.equals(FISH)) {
      return breedTime >= FISH_BREED_TIME;
    }
    return false;
  }

  public void die() {
    this.setState(EMPTY);
  }

  public void become(String state) {
    this.setState(state);
    if (state.equals(SHARK)) {
      setEnergy(SHARK_STARTING_ENERGY);
    }
  }
}

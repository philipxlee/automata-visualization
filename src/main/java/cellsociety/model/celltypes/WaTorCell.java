package cellsociety.model.celltypes;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;

public class WaTorCell extends Cell {

  public static final int SHARK_BREED_TIME = 7;
  public static final int FISH_BREED_TIME = 5;
  public static final int SHARK_STARTING_ENERGY = 10;

  private static final String SHARK = CellStates.SHARK.name();
  private static final String FISH = CellStates.FISH.name();
  private static final String EMPTY = CellStates.EMPTY.name();

  private int breedTime = 0;
  private int energy = SHARK_STARTING_ENERGY;

  /**
   * Create a new cell with the given state at the given row and column
   *
   * @param row   The row position of the cell in the grid.
   * @param col   The column position of the cell in the grid.
   * @param state The initial state of the cell, usually "FISH", "SHARK", or "EMPTY".
   */
  public WaTorCell(int row, int col, String state) {
    super(row, col, state);
    if (state.equals(SHARK)) {
      this.energy = SHARK_STARTING_ENERGY;
    }
  }

  /**
   * Becomes the given state (FISH, SHARK, or EMPTY)
   *
   * @param state the state to become
   */
  public void become(String state) {
    this.setState(state);
    if (state.equals(SHARK)) {
      setEnergy(SHARK_STARTING_ENERGY);
    }
  }

  /**
   * Returns whether the cell can reproduce
   *
   * @param type the type of the cell
   * @return whether the cell can reproduce
   */
  public boolean canReproduce(String type) {
    if (type.equals(SHARK)) {
      return breedTime >= SHARK_BREED_TIME;
    } else if (type.equals(FISH)) {
      return breedTime >= FISH_BREED_TIME;
    }
    return false;
  }

  /**
   * Returns the energy of the shark
   *
   * @return the energy of the shark
   */
  public int getEnergy() {
    return energy;
  }

  /**
   * Sets the energy of the shark
   *
   * @param energy the energy of the shark
   */
  public void setEnergy(int energy) {
    this.energy = energy;
  }

  /**
   * Eats a fish and increases the energy of the shark
   *
   * @param fishCell the fish cell that the shark is eating
   */
  public void eatFish(WaTorCell fishCell) {
    this.energy += 2;
  }

  /**
   * Decreases the energy of the shark
   */
  public void decreaseEnergy() {
    this.energy -= 1;
  }

  /**
   * Returns whether the shark has starved
   *
   * @return whether the shark has starved
   */
  public boolean sharkStarve() {
    return this.energy <= 0;
  }

  /**
   * Increases the breed time of the cell
   */
  public void increaseBreedTime() {
    this.breedTime++;
  }

  /**
   * Resets the breed time of the cell
   */
  public void resetBreedTime() {
    this.breedTime = 0;
  }

  /**
   * Kills the cell
   */
  public void die() {
    this.setState(EMPTY);
  }
}

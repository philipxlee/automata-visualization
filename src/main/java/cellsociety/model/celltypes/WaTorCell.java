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

  /**
   * Create a new cell with the given state at the given row and column
   *
   * @param row
   * @param col
   * @param state
   */
  public WaTorCell(int row, int col, String state) {
    super(row, col, state);
  }

  /**
   * Get the breed time of the cell
   *
   * @return the breed time of the cell
   */
  public int getBreedTime() {
    return this.breedTime;
  }

  /**
   * Set the breed time of the cell
   *
   * @param time the breed time of the cell
   */
  public void setBreedTime(int time) {
    this.breedTime = time;
  }

  /**
   * Set the energy of the cell
   *
   * @param energy the energy of the cell
   */
  public void setEnergy(int energy) {
    this.breedTime = energy;
  }

  /**
   * Get the energy of the cell
   *
   * @return the energy of the cell
   */

  public int getEnergy() {
    return this.energy;
  }

  /**
   * Checks if the animal can reproduce
   *
   * @param animal the animal to check
   * @return a boolean if the animal can reproduce
   */
  public boolean canReproduce(String animal) {
    return (animal.equals(SHARK)) ? breedTime == SHARK_BREED_TIME : breedTime == FISH_BREED_TIME;
  }

  /**
   * Checks if the shark has starved
   *
   * @return a boolean if the shark has starved
   */
  public boolean sharkStarve() {
    return this.energy == 0;
  }

  /**
   * Checks if the animal has been eaten
   *
   * @return a boolean if the animal has been eaten
   */
  public boolean getIsEaten() {
    return this.eaten;
  }

  /**
   * Sets the animal to be eaten
   *
   * @param eaten a boolean if the animal has been eaten
   */
  public void setIsEaten(boolean eaten) {
    this.eaten = eaten;
  }

  /**
   * Resets the animal
   */
  public void resetAnimal() {
    this.energy = 0;
    this.breedTime = 0;
    this.eaten = false;
  }
}

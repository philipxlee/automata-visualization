package cellsociety.model.celltypes.Ants;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AntsCell extends Cell {

  public static final double MAX_PHEROMONE = 1000.0;

  private static final String HOME = CellStates.HOME.name();
  private static final String FOOD = CellStates.FOOD.name();
  private static final String ANT = CellStates.ANT.name();
  private static final String EMPTY = CellStates.EMPTY.name();
  private static final String HIGHPHEROMONE = CellStates.HIGHPHEROMONE.name();
  private static final String LOWPHEROMONE = CellStates.LOWPHEROMONE.name();
  private static final int INITIAL_ANTS = 2;
  private static final int FOOD_AMT = 50;
  private List<Ant> curAnts = new ArrayList<>();
  private double homePheromone = 0;
  private double foodPheromone = 0;
  private int foodAmt = 0;

  /**
   * Creates a new AntsCell with specified row, column, and state.
   *
   * @param row   The row position of the cell in the grid.
   * @param col   The column position of the cell in the grid.
   * @param state The initial state of the cell, "HOME", "FOOD", "ANT", or "EMPTY".
   */
  public AntsCell(int row, int col, String state) {
    super(row, col, state);
    initPheromone(state); //If the cell is a pheromone cell, set the appropriate pheromone level
    initAnts(state); //If the cell is home, add initial ants
    initFood(state); //If the cell is food, set the food amount
  }

  /**
   * Returns the current home pheromone level of the cell.
   *
   * @return The current home pheromone level of the cell.
   */
  public double getHomePheromone() {
    return homePheromone;
  }

  /**
   * Sets the home pheromone level of the cell to the given value.
   *
   * @param pheromoneLevel The new home pheromone level of the cell.
   */
  public void setHomePheromone(double pheromoneLevel) {
    homePheromone = pheromoneLevel;
  }

  /**
   * Returns the current food pheromone level of the cell.
   *
   * @return The current food pheromone level of the cell.
   */
  public double getFoodPheromone() {
    return foodPheromone;
  }

  /**
   * Returns the current food amount of the cell.
   *
   * @return The current food amount of the cell.
   */
  public void setFoodPheromone(double pheromoneLevel) {
    foodPheromone = pheromoneLevel;
  }

  /**
   * List of Ants in the cell.
   *
   * @return current ants in the cell.
   */
  public List<Ant> getCurAnts() {
    return curAnts;
  }

  /**
   * Adds an ant to the cell.
   *
   * @param ant The ant to be added to the cell.
   */
  public void addAnt(Ant ant) {
    curAnts.add(ant);
  }

  /**
   * Updates cell state based on current state and pheromone levels.
   *
   * @return the new state of the cell
   */
  public String updateCellState() {
    if (this.getState().equals(HOME)) {
      return HOME;
    } else if (this.foodAmt > 0) {
      return FOOD;
    } else if (this.curAnts.size() > 5) {
      return ANT;
    }
    return calculatePheromoneLevel();
  }

  public void birthAnts() {
    if (this.getState().equals(HOME)) {
      curAnts.add(new Ant());
      curAnts.add(new Ant());
    }
  }

  public void antsForage(List<AntsCell> neighbors) {
    Iterator<Ant> iterator = curAnts.iterator();
    while (iterator.hasNext()) {
      Ant ant = iterator.next();
      if (!ant.isAlive()) {
        iterator.remove();
        continue;
      }
      ant.ageAnt();
      if (ant.hasFood()) {
        ant.returnHome(this, neighbors, iterator);
      } else {
        ant.findFood(this, neighbors, iterator);
      }
    }
  }

  /**
   * Evaporates the food pheromone level of the cell by the given rate.
   *
   * @param rate The rate at which the food pheromone level should be evaporated.
   */
  public void evaporateFoodPheromone(double rate) {
    double foodEvaporate = rate * this.getFoodPheromone();
    setFoodPheromone(this.getFoodPheromone() - foodEvaporate);
  }

  /**
   * Evaporates the home pheromone level of the cell by the given rate.
   *
   * @param rate The rate at which the home pheromone level should be evaporated.
   */
  public void evaporateHomePheromone(double rate) {
    double homeEvaporate = rate * this.getHomePheromone();
    setHomePheromone(this.getHomePheromone() - homeEvaporate);
  }

  /**
   * Diffuses the food pheromone level of the cell to its neighbors by the given rate.
   *
   * @param rate      The rate at which the food pheromone level should be diffused.
   * @param neighbors The neighbors of the cell to which the pheromone should be diffused.
   */
  public void diffuseFoodPheromone(double rate, List<AntsCell> neighbors) {
    double totalDiffused = 0;
    for (AntsCell neighbor : neighbors) {
      double foodDiffuse = rate * this.getFoodPheromone();
      neighbor.setFoodPheromone(neighbor.getFoodPheromone() + foodDiffuse);
      totalDiffused += foodDiffuse;
    }
    this.setFoodPheromone(this.getFoodPheromone() - totalDiffused);
  }

  /**
   * Diffuses the home pheromone level of the cell to its neighbors by the given rate.
   *
   * @param rate      The rate at which the home pheromone level should be diffused.
   * @param neighbors The neighbors of the cell to which the pheromone should be diffused.
   */
  public void diffuseHomePheromone(double rate, List<AntsCell> neighbors) {
    double totalDiffused = 0;
    for (AntsCell neighbor : neighbors) {
      double homeDiffuse = rate * this.getHomePheromone();
      neighbor.setHomePheromone(neighbor.getHomePheromone() + homeDiffuse);
      totalDiffused += homeDiffuse;
    }
    this.setHomePheromone(this.getHomePheromone() - totalDiffused);
  }

  /**
   * Decrements the food amount of the cell by 2.
   */
  public void decrementFoodAmt() {
    foodAmt -= 2;
  }

  private String calculatePheromoneLevel() {
    double cellPheromone = calculateTotalPheromone();
    if (cellPheromone > 0.25 * MAX_PHEROMONE) {
      return HIGHPHEROMONE;
    } else if (cellPheromone > 0.1 * MAX_PHEROMONE) {
      return LOWPHEROMONE;
    } else {
      return EMPTY;
    }
  }

  private void initPheromone(String state) {
    if (state.equals(HIGHPHEROMONE)) {
      foodPheromone = 0.75 * MAX_PHEROMONE;
    } else if (state.equals(LOWPHEROMONE)) {
      foodPheromone = 0.375 * MAX_PHEROMONE;
    }
  }

  private void initAnts(String state) {
    if (state.equals(ANT)) {
      for (int i = 0; i < INITIAL_ANTS; i++) {
        curAnts.add(new Ant());
      }
    }
  }

  private void initFood(String state) {
    if (state.equals(FOOD)) {
      foodAmt = FOOD_AMT;
    }
  }

  private double calculateTotalPheromone() {
    return homePheromone + foodPheromone;
  }
}

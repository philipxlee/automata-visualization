package cellsociety.model.celltypes;

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
  private static final int FOOD_AMT = 9;

  private List<Ant> curAnts = new ArrayList<>();
  private double homePheromone = 0;
  private double foodPheromone = 0;
  private int foodAmt = 0;
  private int[] direction = new int[]{0, 0};

  /**
   * Creates a new AntsCell with specified row, column, and state.
   *
   * @param row The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
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
  public void setHomePheromone(double pheromoneLevel) { homePheromone = pheromoneLevel; }

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
  public void setFoodPheromone(double pheromoneLevel) { foodPheromone = pheromoneLevel; }

  /**
   * List of Ants in the cell.
   *
   * @return current ants in the cell.
   */
  public List<Ant> getCurAnts() { return curAnts; }

  /**
   * Adds an ant to the cell.
   *
   * @param ant The ant to be added to the cell.
   */
  public void addAnt(Ant ant) { curAnts.add(ant); }

  /**
   * Updates cell state based on current state and pheromone levels.
   *
   * @return
   */
  public String updateCellState() {
    if(this.getState().equals(HOME)) {
      return HOME;
    } else if(this.foodAmt > 0) {
      return FOOD;
    } else if(this.curAnts.size() > 5) {
      return ANT;
    }
    return calculatePheromoneLevel();
  }

  public void birthAnts() {
    if(this.getState().equals(HOME)) {
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
   * @param rate The rate at which the food pheromone level should be diffused.
   * @param neighbors The neighbors of the cell to which the pheromone should be diffused.
   */
  public void diffuseFoodPheromone(double rate, List<AntsCell> neighbors) {
    double totalDiffused = 0;
    for(AntsCell neighbor : neighbors) {
      double foodDiffuse = rate * this.getFoodPheromone();
      neighbor.setFoodPheromone(neighbor.getFoodPheromone() + foodDiffuse);
      totalDiffused += foodDiffuse;
    }
    this.setFoodPheromone(this.getFoodPheromone() - totalDiffused);
  }

  /**
   * Diffuses the home pheromone level of the cell to its neighbors by the given rate.
   *
   * @param rate The rate at which the home pheromone level should be diffused.
   * @param neighbors The neighbors of the cell to which the pheromone should be diffused.
   */
  public void diffuseHomePheromone(double rate, List<AntsCell> neighbors) {
    double totalDiffused = 0;
    for(AntsCell neighbor : neighbors) {
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
    if(cellPheromone > 0.25 * MAX_PHEROMONE) {
      return HIGHPHEROMONE;
    } else if (cellPheromone > 0.1 * MAX_PHEROMONE) {
      return LOWPHEROMONE;
    } else {
      return EMPTY;
    }
  }

  private void initPheromone(String state) {
    if(state.equals(HIGHPHEROMONE)) {
      foodPheromone = 0.75 * MAX_PHEROMONE;
    } else if(state.equals(LOWPHEROMONE)) {
      foodPheromone = 0.375 * MAX_PHEROMONE;
    }
  }

  private void initAnts(String state) {
    if(state.equals(ANT)) {
      for(int i = 0; i < INITIAL_ANTS; i++) {
        curAnts.add(new Ant());
      }
    }
  }

  private void initFood(String state) {
    if(state.equals(FOOD)) {
      foodAmt = FOOD_AMT;
    }
  }

  private double calculateTotalPheromone() {
    return homePheromone + foodPheromone;
  }

}

class Ant {

  public static final double K = 0.001;
  public static final double N = 10.0;

  private int ticksAlive;
  private boolean carryingFood;

  /**
   * Creates a new Ant with no food and 0 ticks alive.
   */
  public Ant() {
    this.ticksAlive = 0;
    this.carryingFood = false;
  }

  /**
   * Returns whether an ant has food.
   *
   * @return if an ant has food
   */
  public boolean hasFood() {
    return carryingFood;
  }

  /**
   * Finds the home cell for the ant to move to.
   *
   * @param antsCell The cell on which the ant is currently located.
   * @param neighbors The neighboring cells of the ant's current location.
   * @param iterator The iterator for the list of ants in the cell.
   */
  public void returnHome(AntsCell antsCell, List<AntsCell> neighbors, Iterator<Ant> iterator) {
    List<AntsCell>[] orientationLists = findOrientation(antsCell, neighbors, CellStates.HOME.name());
    AntsCell moveDirection = findMaxPheromoneCell(orientationLists[0], CellStates.HOME.name());
    if (!canMoveTo(moveDirection)) {
      moveDirection = findMaxPheromoneCell(orientationLists[1], CellStates.HOME.name());
    }
    if (canMoveTo(moveDirection)) {
      dropFoodPheromone(antsCell, neighbors);
      moveAnt(antsCell, moveDirection, iterator);
      if (moveDirection.getState().equals(CellStates.HOME.name())) {
        carryingFood = false;
      }
    }
  }

  /**
   * Finds food for the ant to move to.
   *
   * @param antsCell The cell on which the ant is currently located.
   * @param neighbors The neighboring cells of the ant's current location.
   * @param iterator The iterator for the list of ants in the cell.
   */
  public void findFood(AntsCell antsCell, List<AntsCell> neighbors, Iterator<Ant> iterator) {
    List<AntsCell>[] orientationLists = findOrientation(antsCell, neighbors, CellStates.FOOD.name());
    AntsCell moveDirection = selectLocation(orientationLists[0]);
    if (moveDirection == null || !canMoveTo(moveDirection)) {
      moveDirection = selectLocation(orientationLists[1]);
    }
    if (moveDirection != null && canMoveTo(moveDirection)) {
      dropHomePheromone(antsCell, neighbors);
      moveAnt(antsCell, moveDirection, iterator);
      if (moveDirection.getState().equals(CellStates.FOOD.name())) {
        carryingFood = true;
        moveDirection.decrementFoodAmt();
      }
    }
  }

  /**
   * Drops home pheromone on the cell if the ant is carrying food, or if the cell is a home cell.
   *
   * @param antsCell The cell on which the ant is currently located.
   * @param neighbors The neighboring cells of the ant's current location.
   */
  public void dropHomePheromone(AntsCell antsCell, List<AntsCell> neighbors) {
    if (antsCell.getState().equals(CellStates.HOME.name())) {
      antsCell.setHomePheromone(AntsCell.MAX_PHEROMONE);
    } else {
      AntsCell maxCell = findMaxPheromoneCell(neighbors, CellStates.HOME.name());
      double max = maxCell.getHomePheromone();
      double depositAmt = max - 2 - antsCell.getHomePheromone();
      if(depositAmt > 0) {
        antsCell.setHomePheromone(depositAmt);
      }
    }
  }

  /**
   * Drops food pheromone on the cell if the ant is carrying food, or if the cell is a food cell.
   *
   * @param antsCell The cell on which the ant is currently located.
   * @param neighbors The neighboring cells of the ant's current location.
   */
  public void dropFoodPheromone(AntsCell antsCell, List<AntsCell> neighbors) {
    if (antsCell.getState().equals(CellStates.FOOD.name())) {
      antsCell.setFoodPheromone(AntsCell.MAX_PHEROMONE);
    } else {
      AntsCell maxCell = findMaxPheromoneCell(neighbors, CellStates.FOOD.name());
      double max = maxCell.getFoodPheromone();
      double depositAmt = max - 2 - antsCell.getFoodPheromone();
      if(depositAmt > 0) {
        antsCell.setFoodPheromone(depositAmt);
      }
    }
  }

  /**
   * Selects a location for the ant to move to based on the pheromone levels of the neighboring
   * cells.
   *
   * @param orientation The list of neighboring cells to choose from.
   * @return The cell the ant will move to.
   */
  public AntsCell selectLocation(List<AntsCell> orientation) {
    List<AntsCell> validLocations = new ArrayList<>(orientation);
    for (AntsCell neighbor: orientation) {
      if (!canMoveTo(neighbor)) {
        validLocations.remove(neighbor);
      }
    }
    if(validLocations.size() <= 0) {
      return null;
    }

    double[] cumulativeProbabilities = new double[validLocations.size()];
    double cumulativeProbability = 0.0;
    for (int i = 0; i < validLocations.size(); i++) {
      double probability = Math.pow(K + validLocations.get(i).getFoodPheromone(), N);
      cumulativeProbability += probability;
      cumulativeProbabilities[i] = cumulativeProbability;
    }
    Random random = new Random();
    double rand = random.nextDouble() * cumulativeProbability;
    for (int i = 0; i < validLocations.size(); i++) {
      if (rand <= cumulativeProbabilities[i]) {
        return validLocations.get(i);
      }
    }
    return null;
  }

  /**
   * Finds the cell with the highest pheromone level.
   *
   * @param neighbors neighbors of the cell
   * @param type type of pheromone
   * @return cell with the highest pheromone level
   */
  public AntsCell findMaxPheromoneCell (List<AntsCell> neighbors, String type) {
    AntsCell maxCell = null;
    double max = 0;
    for (AntsCell neighbor: neighbors) {
      double neighborPheromone = type.equals(CellStates.HOME.name()) ? neighbor.getHomePheromone() : neighbor.getFoodPheromone();
      if (neighborPheromone > max) {
        max = neighborPheromone;
        maxCell = neighbor;
      }
    }
    if(maxCell == null) {
      Random random = new Random();
      return neighbors.get(random.nextInt(neighbors.size()));
    }
    return maxCell;
  }

  /**
   * Ages an ant by one tick.
   */
  public void ageAnt() {
    ticksAlive++;
  }

  /**
   * Returns whether an ant is alive.
   *
   * @return if an ant is alive
   */
  public boolean isAlive() {
    return ticksAlive < 500;
  }

  private static final Map<String, int[][]> FORWARD_LOCATIONS = new HashMap<>() {
    {
      put(Arrays.toString(new int[]{1, 0}), new int[][]{{1, 0}, {1, 1}, {1, -1}}); //EAST
      put(Arrays.toString(new int[]{-1, 0}), new int[][]{{-1, 0}, {-1, 1}, {-1, -1}}); //WEST
      put(Arrays.toString(new int[]{0, 1}), new int[][]{{0, 1}, {1, 1}, {-1, 1}}); //SOUTH
      put(Arrays.toString(new int[]{0, -1}), new int[][]{{0, -1}, {1, -1}, {-1, -1}}); //NORTH
      put(Arrays.toString(new int[]{1, 1}), new int[][]{{1, 1}, {1, 0}, {0, 1}}); //SOUTHEAST
      put(Arrays.toString(new int[]{1, -1}), new int[][]{{1, -1}, {1, 0}, {0, -1}}); //NORTHEAST
      put(Arrays.toString(new int[]{-1, 1}), new int[][]{{-1, 1}, {-1, 0}, {0, 1}}); //SOUTHWEST
      put(Arrays.toString(new int[]{-1, -1}), new int[][]{{-1, -1}, {-1, 0}, {0, -1}}); //NORTHWEST
    }
  };

  private List<AntsCell>[] returnForwardNeighborLists(List<AntsCell> neighbors, AntsCell forwardCell,
      AntsCell antsCell) {
    List<AntsCell> forwardList = new ArrayList<>();
    List<AntsCell> neighborList = new ArrayList<>();
    int[] forwardDirection = {forwardCell.getRow() - antsCell.getRow(),
        forwardCell.getCol() - antsCell.getCol()};
    int[][] forwardLocations = FORWARD_LOCATIONS.get(Arrays.toString(forwardDirection));
    for (int i = 0; i < 2; i++) {
      int[] location = forwardLocations[i];
      for (AntsCell neighbor : neighbors) {
        if (neighbor.getRow() == antsCell.getRow() + location[0]
            && neighbor.getCol() == antsCell.getCol() + location[1]) {
          forwardList.add(neighbor);
        } else {
          neighborList.add(neighbor);
        }
      }
    }
    return new List[]{forwardList, neighborList};
  }

  private boolean canMoveTo(AntsCell moveCell){
    return moveCell.getCurAnts().size() < 10;
  }

  private void moveAnt(AntsCell antsCell, AntsCell moveCell, Iterator<Ant> iterator) {
    moveCell.addAnt(this);
    iterator.remove();
  }

  private List<AntsCell>[] findOrientation (AntsCell antsCell, List<AntsCell> neighbors, String pheromoneType) {
    AntsCell forwardCell = findMaxPheromoneCell(neighbors, pheromoneType);
    List<AntsCell>[] orientationLists = returnForwardNeighborLists(neighbors, forwardCell, antsCell);
    return orientationLists;
  }
}

package cellsociety.model.celltypes.ants;

import cellsociety.model.CellStates;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Ant {

  public static final double K = 0.001;
  public static final double N = 10.0;
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
   * @param antsCell  The cell on which the ant is currently located.
   * @param neighbors The neighboring cells of the ant's current location.
   * @param iterator  The iterator for the list of ants in the cell.
   */
  public void returnHome(AntsCell antsCell, List<AntsCell> neighbors, Iterator<Ant> iterator) {
    List<AntsCell>[] orientationLists = findOrientation(antsCell, neighbors,
        CellStates.HOME.name());
    AntsCell moveDirection = findMaxPheromoneCell(orientationLists[0], CellStates.HOME.name());
    if (!canMoveTo(moveDirection)) {
      moveDirection = findMaxPheromoneCell(orientationLists[1], CellStates.HOME.name());
    }
    if (canMoveTo(moveDirection)) {
      dropFoodPheromone(antsCell, neighbors);
      moveAnt(moveDirection, iterator);
      if (moveDirection.getState().equals(CellStates.HOME.name())) {
        carryingFood = false;
      }
    }
  }

  /**
   * Finds food for the ant to move to.
   *
   * @param antsCell  The cell on which the ant is currently located.
   * @param neighbors The neighboring cells of the ant's current location.
   * @param iterator  The iterator for the list of ants in the cell.
   */
  public void findFood(AntsCell antsCell, List<AntsCell> neighbors, Iterator<Ant> iterator) {
    List<AntsCell>[] orientationLists = findOrientation(antsCell, neighbors,
        CellStates.FOOD.name());
    AntsCell moveDirection = selectLocation(orientationLists[0]);
    if (moveDirection == null || !canMoveTo(moveDirection)) {
      moveDirection = selectLocation(orientationLists[1]);
    }
    if (moveDirection != null && canMoveTo(moveDirection)) {
      dropHomePheromone(antsCell, neighbors);
      moveAnt(moveDirection, iterator);
      if (moveDirection.getState().equals(CellStates.FOOD.name())) {
        carryingFood = true;
        moveDirection.decrementFoodAmt();
      }
    }
  }

  /**
   * Drops home pheromone on the cell if the ant is carrying food, or if the cell is a home cell.
   *
   * @param antsCell  The cell on which the ant is currently located.
   * @param neighbors The neighboring cells of the ant's current location.
   */
  public void dropHomePheromone(AntsCell antsCell, List<AntsCell> neighbors) {
    if (antsCell.getState().equals(CellStates.HOME.name())) {
      antsCell.setHomePheromone(AntsCell.MAX_PHEROMONE);
    } else {
      AntsCell maxCell = findMaxPheromoneCell(neighbors, CellStates.HOME.name());
      double max = maxCell.getHomePheromone();
      double depositAmt = max - 2 - antsCell.getHomePheromone();
      if (depositAmt > 0) {
        antsCell.setHomePheromone(depositAmt);
      }
    }
  }

  /**
   * Drops food pheromone on the cell if the ant is carrying food, or if the cell is a food cell.
   *
   * @param antsCell  The cell on which the ant is currently located.
   * @param neighbors The neighboring cells of the ant's current location.
   */
  public void dropFoodPheromone(AntsCell antsCell, List<AntsCell> neighbors) {
    if (antsCell.getState().equals(CellStates.FOOD.name())) {
      antsCell.setFoodPheromone(AntsCell.MAX_PHEROMONE);
    } else {
      AntsCell maxCell = findMaxPheromoneCell(neighbors, CellStates.FOOD.name());
      double max = maxCell.getFoodPheromone();
      double depositAmt = max - 2 - antsCell.getFoodPheromone();
      if (depositAmt > 0) {
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
    for (AntsCell neighbor : orientation) {
      if (!canMoveTo(neighbor)) {
        validLocations.remove(neighbor);
      }
    }
    if (validLocations.size() <= 0) {
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
   * @param type      type of pheromone
   * @return cell with the highest pheromone level
   */
  public AntsCell findMaxPheromoneCell(List<AntsCell> neighbors, String type) {
    AntsCell maxCell = null;
    double max = 0;
    for (AntsCell neighbor : neighbors) {
      double neighborPheromone = type.equals(CellStates.HOME.name()) ? neighbor.getHomePheromone()
          : neighbor.getFoodPheromone();
      if (neighborPheromone > max) {
        max = neighborPheromone;
        maxCell = neighbor;
      }
    }
    if (maxCell == null) {
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

  private List<AntsCell>[] returnForwardNeighborLists(List<AntsCell> neighbors,
      AntsCell forwardCell,
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

  private boolean canMoveTo(AntsCell moveCell) {
    return moveCell.getCurAnts().size() < 10;
  }

  private void moveAnt(AntsCell moveCell, Iterator<Ant> iterator) {
    moveCell.addAnt(this);
    iterator.remove();
  }

  private List<AntsCell>[] findOrientation(AntsCell antsCell, List<AntsCell> neighbors,
      String pheromoneType) {
    AntsCell forwardCell = findMaxPheromoneCell(neighbors, pheromoneType);
    List<AntsCell>[] orientationLists = returnForwardNeighborLists(neighbors, forwardCell,
        antsCell);
    return orientationLists;
  }
}
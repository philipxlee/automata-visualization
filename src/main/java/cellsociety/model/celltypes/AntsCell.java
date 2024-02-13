package cellsociety.model.celltypes;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Vector;

public class AntsCell extends Cell {

  public static final int SHARK_BREED_TIME = 7;
  public static final int FISH_BREED_TIME = 5;
  public static final int SHARK_STARTING_ENERGY = 10;
  private static final String HOME = CellStates.HOME.name();
  private static final String FOOD = CellStates.FOOD.name();
  private static final String ANT = CellStates.ANT.name();
  private static final String EMPTY = CellStates.EMPTY.name();
  private static final String HIGHPHEROMONE = CellStates.HIGHPHEROMONE.name();
  private static final String LOWPHEROMONE = CellStates.LOWPHEROMONE.name();
  public static final double MAX_PHEROMONE = 1000.0;
  private static final int INITIAL_ANTS = 2;
  private static final int FOOD_AMT = 10;
  private int breedTime = 0;
  private List<Ant> curAnts = new ArrayList<>();
  private double homePheromone = 0;
  private double foodPheromone = 0;
  private int foodAmt = 0;
  private int[] direction = new int[]{0, 0};
  public AntsCell(int row, int col, String state) {
    super(row, col, state);
    initPheromone(state); //If the cell is a pheromone cell, set the appropriate pheromone level
    initAnts(state); //If the cell is home, add initial ants
    initFood(state); //If the cell is food, set the food amount
  }

  public double getHomePheromone() {
    return homePheromone;
  }

  public void setHomePheromone(double pheromoneLevel) { homePheromone = pheromoneLevel; }

  public double getFoodPheromone() {
    return foodPheromone;
  }

  public int[] getDirection() { return direction; }
  public void setFoodPheromone(double pheromoneLevel) { foodPheromone = pheromoneLevel; }

  public  List<Ant> getCurAnts() { return curAnts; }

  public String updateCellState() {
    if(this.getState().equals(HOME)) {
      return HOME;
    } else if(this.foodAmt > 0) {
      return FOOD;
    } else if(this.curAnts.size() > 0) {
      return ANT;
    }
    return calculatePheromoneLevel();
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

  public void birthAnts() {
    if(this.getState().equals(HOME)) {
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

  public void evaporateFoodPheromone(double rate) {
    double foodEvaporate = rate * this.getFoodPheromone();
    setFoodPheromone(foodEvaporate);
  }

  public void evaporateHomePheromone(double rate) {
    double homeEvaporate = rate * this.getHomePheromone();
    setHomePheromone(homeEvaporate);
  }

  public void diffuseFoodPheromone(double rate, List<AntsCell> neighbors) {
    double totalDiffused = 0;
    for(AntsCell neighbor : neighbors) {
      double foodDiffuse = rate * this.getFoodPheromone();
      neighbor.setFoodPheromone(neighbor.getFoodPheromone() + foodDiffuse);
      totalDiffused += foodDiffuse;
    }
    this.setFoodPheromone(this.getFoodPheromone() - totalDiffused);
  }

  public void diffuseHomePheromone(double rate, List<AntsCell> neighbors) {
    double totalDiffused = 0;
    for(AntsCell neighbor : neighbors) {
      double homeDiffuse = rate * this.getHomePheromone();
      neighbor.setHomePheromone(neighbor.getHomePheromone() + homeDiffuse);
      totalDiffused += homeDiffuse;
    }
    this.setHomePheromone(this.getHomePheromone() - totalDiffused);
  }

  public void decrementFoodAmt() {
    foodAmt -= 1;
  }



}

class Ant {
  private int ticksAlive;
  private boolean carryingFood;
  public static final double K = 0.001;
  public static final double N = 10.0;

  public Ant() {
    this.ticksAlive = 0;
    this.carryingFood = false;
  }

  public boolean hasFood() {
    return carryingFood;
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
    moveCell.getCurAnts().add(this);
    iterator.remove();
  }

  private List<AntsCell>[] findOrientation (AntsCell antsCell, List<AntsCell> neighbors, String pheromoneType) {
    AntsCell forwardCell = findMaxPheromoneCell(neighbors, pheromoneType);
    List<AntsCell>[] orientationLists = returnForwardNeighborLists(neighbors, forwardCell, antsCell);
    return orientationLists;
  }

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
        antsCell.decrementFoodAmt();
      }
    }
  }

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

    return  null;
  }

  public AntsCell findMaxPheromoneCell (List<AntsCell> neighbors, String type) {
    AntsCell maxCell = null;
    double max = 0;
    for (AntsCell neighbor: neighbors) {
      double neighborPheromone = type.equals(CellStates.HOME.name()) ? neighbor.getHomePheromone() : neighbor.getFoodPheromone();
      if (neighborPheromone >= max) {
        max = neighborPheromone;
        maxCell = neighbor;
      }
    }
    return maxCell;
  }

  public void ageAnt() {
    ticksAlive++;
  }
  public boolean isAlive() {
    return ticksAlive < 500;
  }


}

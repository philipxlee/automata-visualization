package cellsociety.model.celltypes;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;
import java.util.ArrayList;
import java.util.HashMap;
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
  private int breedTime = 0;
  private List<Ant> curAnts = new ArrayList<>();
  private double homePheromone = 0;
  private double foodPheromone = 0;
  private int[] direction = new int[]{0, 0};
  public AntsCell(int row, int col, String state) {
    super(row, col, state);
    initPheromone(state); //If the cell is a pheromone cell, set the appropriate pheromone level
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
    if(this.getState().equals(HOME) || this.getState().equals(FOOD)) {
      return this.getState();
    } else if(this.curAnts.size() > 0) {
      return ANT;
    }
    return calculatePheromoneLevel();
  }

  private String calculatePheromoneLevel() {
    double cellPheromone = calculateTotalPheromone();
    if(cellPheromone > 0.5) {
      return HIGHPHEROMONE;
    } else if (cellPheromone > 0.25) {
      return LOWPHEROMONE;
    } else {
      return EMPTY;
    }
  }

  private void initPheromone(String state) {
    if(state.equals(HIGHPHEROMONE)) {
      foodPheromone = 0.75;
    } else if(state.equals(LOWPHEROMONE)) {
      foodPheromone = 0.375;
    }
  }
  private double calculateTotalPheromone() {
    return homePheromone + foodPheromone;
  }

  public void antsForage(List<AntsCell> neighbors) {
    for(Ant ant : curAnts) {
      if(ant.hasFood()) {
        ant.returnHome(this, neighbors);
      } else {
        ant.findFood(this, neighbors);
      }
    }
  }

}

class Ant {

  private boolean carryingFood;
  public static final double K = 0.001;
  public static final double N = 10.0;

  public boolean hasFood() {
    return carryingFood;
  }

  private static final Map<int[], int[][]> FORWARD_LOCATIONS = new HashMap<>() {
    {
      put(new int[]{1, 0}, new int[][]{{1, 0}, {1, 1}, {1, -1}}); //EAST
      put(new int[]{-1, 0}, new int[][]{{-1, 0}, {-1, 1}, {-1, -1}}); //WEST
      put(new int[]{0, 1}, new int[][]{{0, 1}, {1, 1}, {-1, 1}}); //SOUTH
      put(new int[]{0, -1}, new int[][]{{0, -1}, {1, -1}, {-1, -1}}); //NORTH
      put(new int[]{1, 1}, new int[][]{{1, 1}, {1, 0}, {0, 1}}); //SOUTHEAST
      put(new int[]{1, -1}, new int[][]{{1, -1}, {1, 0}, {0, -1}}); //NORTHEAST
      put(new int[]{-1, 1}, new int[][]{{-1, 1}, {-1, 0}, {0, 1}}); //SOUTHWEST
      put(new int[]{-1, -1}, new int[][]{{-1, -1}, {-1, 0}, {0, -1}}); //NORTHWEST
    }
  };

  private List<AntsCell>[] returnForwardNeighborLists(List<AntsCell> neighbors, AntsCell forwardCell,
      AntsCell antsCell) {
    List<AntsCell> forwardList = new ArrayList<>();
    List<AntsCell> neighborList = new ArrayList<>();
    int[] forwardDirection = {forwardCell.getRow() - antsCell.getRow(),
        forwardCell.getCol() - antsCell.getCol()};
    int[][] forwardLocations = FORWARD_LOCATIONS.get(forwardDirection);
    for (int[] location : forwardLocations) {
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

  private void moveAnt(AntsCell antsCell, AntsCell moveCell) {
    moveCell.getCurAnts().add(this);
    antsCell.getCurAnts().remove(this);
  }

  private List<AntsCell>[] findOrientation (AntsCell antsCell, List<AntsCell> neighbors, String pheromoneType) {
    AntsCell forwardCell = findMaxPheromoneCell(neighbors, pheromoneType);
    List<AntsCell>[] orientationLists = returnForwardNeighborLists(neighbors, forwardCell, antsCell);
    return orientationLists;
  }

  public void returnHome(AntsCell antsCell, List<AntsCell> neighbors) {
    List<AntsCell>[] orientationLists = findOrientation(antsCell, neighbors, CellStates.HOME.name());
    AntsCell moveDirection = findMaxPheromoneCell(orientationLists[0], CellStates.HOME.name());
    if (!canMoveTo(moveDirection)) {
      moveDirection = findMaxPheromoneCell(orientationLists[1], CellStates.HOME.name());
    }
    if (canMoveTo(moveDirection)) {
      dropFoodPheromone(antsCell, neighbors);
      moveAnt(antsCell, moveDirection);
      if (moveDirection.getState().equals(CellStates.HOME.name())) {
        carryingFood = false;
      }
    }
  }

  public void findFood(AntsCell antsCell, List<AntsCell> neighbors) {
    List<AntsCell>[] orientationLists = findOrientation(antsCell, neighbors, CellStates.FOOD.name());
    AntsCell moveDirection = selectLocation(orientationLists[0]);
    if (!canMoveTo(moveDirection)) {
      moveDirection = selectLocation(orientationLists[1]);
    }
    if (canMoveTo(moveDirection)) {
      dropHomePheromone(antsCell, neighbors);
      moveAnt(antsCell, moveDirection);
      if (moveDirection.getState().equals(CellStates.FOOD.name())) {
        carryingFood = true;
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
    double rand = random.nextDouble();
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
      if (neighborPheromone > max) {
        max = neighborPheromone;
        maxCell = neighbor;
      }
    }
    return maxCell;
  }


}

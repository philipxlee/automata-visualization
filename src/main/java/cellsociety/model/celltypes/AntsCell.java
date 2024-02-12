package cellsociety.model.celltypes;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
  private int breedTime = 0;
  private List<Ant> curAnts = new ArrayList<>();
  private double homePheromone = 0;
  private double foodPheromone = 0;
  public AntsCell(int row, int col, String state) {
    super(row, col, state);
    initPheromone(state); //If the cell is a pheromone cell, set the appropriate pheromone level
  }

  public double getHomePheromone() {
    return homePheromone;
  }

  public double getFoodPheromone() {
    return foodPheromone;
  }

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

  private List<AntsCell> getOrientation(List<AntsCell> neighbors, String pheromoneType) {
    List<AntsCell> orientation = new ArrayList<>();

    double maxPheromone = 0;
    AntsCell maxPheromoneCell = null;

    for(AntsCell neighbor : neighbors) {
      double neighborPheromone = pheromoneType.equals("HOME") ? neighbor.getHomePheromone() : neighbor.getFoodPheromone();
      if(neighborPheromone > maxPheromone) {
        maxPheromone = neighborPheromone;
        maxPheromoneCell = neighbor;
      }
    }

    orientation.add(maxPheromoneCell); //forward location (N)
    //TODO: add the other 2 forward locations (NE, NW)

    return orientation;
  }

  public void antsForage(List<AntsCell> neighbors) {
    for(Ant ant : curAnts) {
      if(ant.hasFood()) {
        ant.returnHome(this.getState(), getOrientation(neighbors, "HOME"));
      } else {
        ant.findFood(this.getState(), getOrientation(neighbors, "FOOD"));
      }
    }
  }

}

class Ant {
  private boolean carryingFood;
  public boolean hasFood() {
    return carryingFood;
  }
  public void returnHome(String cellState, List<AntsCell> neighbors) {
    List<AntsCell> orientation = new ArrayList<>();
    if(cellState.equals(CellStates.FOOD.name())) {
      orientation.addAll(neighbors);
    }
    //TODO: move ant towards cell with highest home pheromone from orientation
    //as long as that cell is not overcrowded by ants or an obstacle(not implemented)
    //TODO: drop-food-pheromone
    //TODO: move ant to cell high home pheromone cell
    //TODO: if ant is now on home cell, drop food

  }
  public void findFood(String cellState, List<AntsCell> neighbors) {
    List<AntsCell> orientation = new ArrayList<>();
    if(cellState.equals(CellStates.HOME.name())) {
      orientation.addAll(neighbors);
    }
    //TODO: X <- SELECT-LOCATION(foward)
    //TODO: if X does not work - overcrowded, X <- SELECT-LOCATION(neighbors)
    //TODO: if X works
    //TODO: drop-home-pheromone
    //TODO: move ant to cell high food pheromone cell
    //TODO: if ant is now on food, carry food
  }

  public void dropPheromone(String cellState, List<AntsCell> neighbors, String pheromoneType) {
    if(cellState.equals(CellStates.HOME.name())) {
      //TODO: set home phero to max
    }
    //TODO: get max home pheromones of neighbors
    //TODO: D = maxHomePheromone - 2 - homePheromoneHere
    //TODO: if D > 0, set homePheroHere to D
    //TODO: to go from "drop-home" to "drop-food" do this:
    //TODO: “home phero”→“food phero” and “home”→“food source”
  }

  public List<AntsCell> selectLocation(List<AntsCell> orientation) {
    //TODO: return L = locations it can move to
    //TODO: L = L - obstacles
    //TODO: L = L - overcrowded
    //TODO: if L is empty, return NULL
    //TODO: select a location from L, where each location has probability of being selected:
    //TODO: (K + foodPheroThere)^N
    return null;
  }


}

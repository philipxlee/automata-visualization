package cellsociety.model.celltypes;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;
import java.util.ArrayList;
import java.util.List;
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
        ant.returnHome(this, getOrientation(neighbors, "HOME"));
      } else {
        ant.findFood(this, getOrientation(neighbors, "FOOD"));
      }
    }
  }

  public List<AntsCell> returnForward(List<AntsCell> neighbors) {
    List<AntsCell> forwardList = new ArrayList<>();
    if (direction[0] == 0 && direction[1] == 0) {
      return neighbors;
    } else if (direction[0] == 0) {
      for (AntsCell neighbor: neighbors) {
        if (neighbor.getRow() == (this.getDirection()[1] + this.getRow())) {
          forwardList.add(neighbor);
        }
      }
    } else if (direction[1] == 0) {
      for (AntsCell neighbor: neighbors) {
        if (neighbor.getCol() == (this.getDirection()[0] + this.getCol())) {
          forwardList.add(neighbor);
        }
      }
    } else {
      for (AntsCell neighbor: neighbors) {
        if (neighbor.getCol() == (this.getDirection()[0] + this.getCol()) && neighbor.getRow() == 0) {
          forwardList.add(neighbor);
        } else if (neighbor.getRow() == (this.getDirection()[0] + this.getRow()) && neighbor.getCol() == 0) {
          forwardList.add(neighbor);
        } else if (neighbor.getCol() == (this.getDirection()[0] + this.getCol()) &&
            neighbor.getRow() == (this.getDirection()[0] + this.getRow())) {
          forwardList.add(neighbor);
        }
      }
    }

    return forwardList;
  }

}

class Ant {
  private boolean carryingFood;
  public static final double K = 0.001;
  public static final double N = 10.0;
  public boolean hasFood() {
    return carryingFood;
  }
  public void returnHome(AntsCell antsCell, List<AntsCell> neighbors) {
    List<AntsCell> orientation = new ArrayList<>();
    AntsCell direction;
    if(antsCell.getState().equals(CellStates.FOOD.name())) {
      direction = maxHomePheromoneCell(neighbors);
      return;
    }

    direction = maxHomePheromoneCell(antsCell.returnForward(neighbors));
    if (direction == null) {
      direction = maxHomePheromoneCell(neighbors);
    }

    if (direction != null) {
      dropFoodPheromone(antsCell, neighbors);
      //TODO: move ant to direction
      if (antsCell.getState().equals(CellStates.HOME.name())) {
        carryingFood = false;
      }
    }



    //TODO: move ant towards cell with highest home pheromone from orientation
    //as long as that cell is not overcrowded by ants or an obstacle(not implemented)
    //TODO: drop-food-pheromone

    //TODO: if ant is now on home cell, drop food

  }
  public void findFood(AntsCell antsCell, List<AntsCell> neighbors) {
    List<AntsCell> orientation = new ArrayList<>();
    AntsCell direction;
    if(antsCell.getState().equals(CellStates.HOME.name())) {
      direction = maxFoodPheromoneCell(neighbors);
      return;
    }
    //TODO: X <- SELECT-LOCATION(foward)
    direction = selectLocation(antsCell.returnForward(neighbors));
    //TODO: if X does not work - overcrowded, X <- SELECT-LOCATION(neighbors)
    if (direction == null) {
      direction = selectLocation(neighbors);
    }

    //TODO: if X works
    if (direction != null) {
      //TODO: drop-home-pheromone
      dropHomePheromone(antsCell, neighbors);
      //TODO: move ant to cell high food pheromone cell

      //TODO: if ant is now on food, carry food
      if (antsCell.getState().equals(CellStates.FOOD.name())) {
        carryingFood = true;
      }
    }

  }

  public void dropHomePheromone(AntsCell antsCell, List<AntsCell> neighbors) {
    if(antsCell.getState().equals(CellStates.HOME.name())) {
      antsCell.setHomePheromone(AntsCell.MAX_PHEROMONE);
    } else {
      double max = 0;
      for (AntsCell neighbor: neighbors) {
        if (neighbor.getHomePheromone() > max) {
          max = neighbor.getHomePheromone();
          double des = max - 2;
          double deposit = des - antsCell.getHomePheromone();

          if (deposit > 0) {
            antsCell.setHomePheromone(antsCell.getHomePheromone() + deposit);
          }
        }
      }
      //TODO: get max home pheromones of neighbors
      //TODO: D = maxHomePheromone - 2 - homePheromoneHere
      //TODO: if D > 0, set homePheroHere to D
      //TODO: to go from "drop-home" to "drop-food" do this:
      //TODO: “home phero”→“food phero” and “home”→“food source”
    }
  }

  public void dropFoodPheromone(AntsCell antsCell, List<AntsCell> neighbors) {
    if (antsCell.getState().equals(CellStates.FOOD.name())) {
      antsCell.setFoodPheromone(AntsCell.MAX_PHEROMONE);
    } else {
      double max = 0;
      for (AntsCell neighbor: neighbors) {
        if (neighbor.getFoodPheromone() > max) {
          max = neighbor.getFoodPheromone();
          double des = max - 2;
          double deposit = des - antsCell.getFoodPheromone();

          if (deposit > 0) {
            antsCell.setFoodPheromone(antsCell.getFoodPheromone() + deposit);
          }
        }
      }
      //TODO: get max home pheromones of neighbors
      //TODO: D = maxHomePheromone - 2 - homePheromoneHere
      //TODO: if D > 0, set homePheroHere to D
      //TODO: to go from "drop-home" to "drop-food" do this:
      //TODO: “home phero”→“food phero” and “home”→“food source”
    }
  }

  public AntsCell selectLocation(List<AntsCell> orientation) {
    //TODO: return L = locations it can move to
    //TODO: L = L - obstacles
    //TODO: L = L - overcrowded
    for (AntsCell neighbor: orientation) {
      if (neighbor.getCurAnts().size() >= 10) {
        orientation.remove(neighbor);
      }
    }

    if (orientation.isEmpty()) {
      //TODO: if L is empty, return NULL
      return null;
    } else {

      double[] cumulativeProbabilities = new double[orientation.size()];
      double cumulativeProbability = 0.0;
      int i = 0;

      // Calculate cumulative probabilities for each location in LocSet
      for (AntsCell neighbor : orientation) {
        double probability = Math.pow(K + neighbor.getFoodPheromone(), N);
        cumulativeProbability += probability;
        cumulativeProbabilities[i++] = cumulativeProbability;
      }

      // Generate a random number between 0 and 1
      Random random = new Random();
      double rand = random.nextDouble();

      // Select a location based on the generated random number
      for (i = 0; i < orientation.size(); i++) {
        //TODO: select a location from L, where each location has probability of being selected:
        //TODO: (K + foodPheroThere)^N
        if (rand <= cumulativeProbabilities[i]) {
          return orientation.get(i);
        }
      }
    }
    return  null;
  }

  public AntsCell maxHomePheromoneCell (List<AntsCell> neighbors) {
    AntsCell maxCell = null;
    double max = 0;
    for (AntsCell neighbor: neighbors) {
      if (neighbor.getHomePheromone() > max) {
        max = neighbor.getHomePheromone();
        maxCell = neighbor;
      }
    }

    return  maxCell;
  }

  public AntsCell maxFoodPheromoneCell (List<AntsCell> neighbors) {
    AntsCell maxCell = null;
    double max = 0;
    for (AntsCell neighbor : neighbors) {
      if (neighbor.getFoodPheromone() > max) {
        max = neighbor.getFoodPheromone();
        maxCell = neighbor;
      }
    }

    return maxCell;
  }


}

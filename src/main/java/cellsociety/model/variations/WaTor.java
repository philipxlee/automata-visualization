package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.WaTorCell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaTor implements Simulation<WaTorCell> {

  private final String EMPTY = CellStates.EMPTY.name();
  private final String FISH = CellStates.FISH.name();
  private final String SHARK = CellStates.SHARK.name();
  private final int ONE_UNIT_OF_CHRONON = 1;
  private final int ONE_UNIT_OF_ENERGY = 1;
  private final int CHRONONS_NEEDED_TO_REPRODUCE = 5;
  private final int ENERGY_GAINED_FROM_EATING_FISH = 1;

  private Random rand = new Random();

  /**
   * Creates a new WaTorCell with specified row, column, and state.
   * This method is specific to the WaTor simulation, where cells can represent fish, sharks, or water.
   * @param row The row position of the cell in the grid.
   * @param col The column position of the cell in the grid.
   * @param state The initial state of the cell, representing fish, sharks, or water.
   * @return A new instance of WaTorCell with the given parameters.
   */
  @Override
  public WaTorCell createVariationCell(int row, int col, String state) {
    return new WaTorCell(row, col, state);
  }

  @Override
  public String determineState(WaTorCell cell, String currentState, List<WaTorCell> neighbors) {
    switch(currentState) {
      case "FISH":
        boolean fishMoved = handleFishMovement(cell, neighbors);
        return (fishMoved) ? EMPTY : currentState;
      case "SHARK":
        boolean sharkMoved = handleSharkMovement(cell, neighbors);
        return (sharkMoved) ? cell.getState() : currentState;
    }
    return currentState;
  }

  private boolean handleSharkMovement(WaTorCell cell, List<WaTorCell> neighbors) {
    // Count fish and empty spaces
    List<WaTorCell> fishes = new ArrayList<>();
    List<WaTorCell> emptySpaces = new ArrayList<>();
    calculateSharkNeighbors(cell, neighbors, fishes, emptySpaces);

    // If fish exists, move to it
    if (!fishes.isEmpty()) {
      int randomIndex = rand.nextInt(fishes.size());
      WaTorCell sharkNextCell = fishes.get(randomIndex);
      sharkNextCell.setState(SHARK);
      sharkNextCell.setEnergy(cell.getEnergy() + ENERGY_GAINED_FROM_EATING_FISH);
      sharkNextCell.setReproductionTime(cell.getReproductionTime() + ONE_UNIT_OF_CHRONON);

      if (checkIfSharkReproduces(sharkNextCell)) {
        cell.setState(SHARK);
        return true;
      }

      cell.setState(EMPTY);
      cell.setEnergy(0);
      cell.setReproductionTime(0);
      return true;
    }

    // Otherwise, move to an empty space if it exists
    if (!emptySpaces.isEmpty()) {
      int randomIndex = rand.nextInt(emptySpaces.size());
      WaTorCell sharkNextCell = emptySpaces.get(randomIndex);
      sharkNextCell.setState(SHARK);
      sharkNextCell.setEnergy(cell.getEnergy() - ONE_UNIT_OF_ENERGY);

      // Check if shark dies after moving, if so, then next cell should be empty too
      if (checkIfSharkDies(sharkNextCell)) {
        sharkNextCell.setState(EMPTY);
        sharkNextCell.setEnergy(0);
        sharkNextCell.setReproductionTime(0);
      }

      cell.setEnergy(0);
      cell.setReproductionTime(0);
      cell.setState(EMPTY);
      return true;
    }

    // Return false if shark hasn't moved
    cell.setEnergy(cell.getEnergy() - ONE_UNIT_OF_ENERGY);
    return false;
  }

  private boolean checkIfSharkReproduces(WaTorCell sharkNextCell) {
    if (sharkNextCell.getReproductionTime() == CHRONONS_NEEDED_TO_REPRODUCE) {
      sharkNextCell.setReproductionTime(0);
      return true;
    }
    return false;
  }

  private boolean checkIfSharkDies(WaTorCell sharkNextCell) {
    if (sharkNextCell.getEnergy() <= 0) {
      sharkNextCell.setReproductionTime(0);
      sharkNextCell.setEnergy(0);
      return true;
    }
    return false;
  }

  private void calculateSharkNeighbors(WaTorCell cell, List<WaTorCell> neighbors, List<WaTorCell> fishes, List<WaTorCell> emptySpaces) {
    for (WaTorCell neighbor : neighbors) {
      if (isCardinalNeighbor(cell, neighbor)) {
        if (neighbor.getState().equals(FISH)) {
          fishes.add(neighbor);
        } else if (neighbor.getState().equals(EMPTY)) {
          emptySpaces.add(neighbor);
        }
      }
    }
  }

  // Return true if fish moved and false otherwise
  private boolean handleFishMovement(WaTorCell cell, List<WaTorCell> neighbors) {
    List<WaTorCell> emptySpaces = new ArrayList<>();
    calculateFishEmptyNeighbors(cell, neighbors, emptySpaces);
    if (!emptySpaces.isEmpty()) {
      int randomIndex = rand.nextInt(emptySpaces.size());
      WaTorCell fishNextCell = emptySpaces.get(randomIndex);
      handleFishStateAndReproduction(cell, fishNextCell);
      return true;
    }
    return false;
  }

  // If current cell's reproduction time is equal to chronons needed to reproduce,
  // set the current cell's reproduction time to 0 and make it a fish, simulating reproduction.
  // Otherwise, make current cell empty and make the destination the fish is going to have
  // the current cell's reproduction time + 1, effectively "moving" the fish and increasing
  // the reproduction time. Set the current cell's reproduction time back to 0, resetting it.
  private void handleFishStateAndReproduction(WaTorCell cell, WaTorCell fishNextCell) {
    int newReproductionTime = 0;
    fishNextCell.setState(cell.getState());
    if (cell.getReproductionTime() >= CHRONONS_NEEDED_TO_REPRODUCE) {
      newReproductionTime = 0;
      cell.setState(FISH);
    } else {
      newReproductionTime = cell.getReproductionTime() + ONE_UNIT_OF_CHRONON;
      cell.setState(EMPTY);
    }
    fishNextCell.setReproductionTime(newReproductionTime);
    cell.setReproductionTime(0);
  }

  private void calculateFishEmptyNeighbors(WaTorCell cell, List<WaTorCell> neighbors, List<WaTorCell> emptySpaces) {
    for (WaTorCell neighbor : neighbors) {
      if (isCardinalNeighbor(cell, neighbor) && neighbor.getState().equals(EMPTY)) {
        emptySpaces.add(neighbor);
      }
    }
  }

  // NOTE: This is REPEATED in SpreadingOfFire! Make it DRY.
  private boolean isCardinalNeighbor(WaTorCell centralCell, WaTorCell neighbor) {
    // Check if the neighbor is directly north, south, east, or west of the central cell
    boolean sameRow = centralCell.getRow() == neighbor.getRow();
    boolean sameCol = centralCell.getCol() == neighbor.getCol();
    boolean adjacentRow = Math.abs(centralCell.getRow() - neighbor.getRow()) == 1;
    boolean adjacentCol = Math.abs(centralCell.getCol() - neighbor.getCol()) == 1;
    return (sameRow && adjacentCol) || (sameCol && adjacentRow);
  }
}

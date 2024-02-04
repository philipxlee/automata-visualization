package cellsociety.model.variations;

import cellsociety.model.Cell;
import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.WaTorCells;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaTor implements Simulation {

  private final String EMPTY = CellStates.EMPTY.name();
  private final int CHRONONS_NEEDED_TO_REPRODUCE = 5;

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
  public Cell createVariationCell(int row, int col, String state) {
    return new WaTorCells(row, col, state);
  }


  @Override
  public String determineState(Cell cell, String currentState, List<Cell> neighbors) {
    switch(currentState) {
      case "FISH":
        boolean fishMoved = handleFishMovement(cell, neighbors);
        return (fishMoved) ? EMPTY : currentState;
      case "SHARK":
    }
    return "PLACEHOLDER";

  }

  // Return true if fish moved and false otherwise
  private boolean handleFishMovement(Cell cell, List<Cell> neighbors) {
    List<Cell> emptySpaces = new ArrayList<>();
    calculateFishEmptyNeighbors(cell, neighbors, emptySpaces);
    if (!emptySpaces.isEmpty()) {
      int randomIndex = rand.nextInt(emptySpaces.size());
      Cell fishNextCell = emptySpaces.get(randomIndex);
      fishNextCell.setState(cell.getState());

      // cant access watorcell getters?
//      int nextCellReproductiveTime = cell.getReproductionTime() + 1;

      cell.setState(EMPTY);
      return true;
    }
    return false;
  }

  private void calculateFishEmptyNeighbors(Cell cell, List<Cell> neighbors, List<Cell> emptySpaces) {
    for (Cell neighbor : neighbors) {
      if (isCardinalNeighbor(cell, neighbor)) {
        emptySpaces.add(neighbor);
      }
    }
  }

  // NOTE: This is REPEATED in SpreadingOfFire! Make it DRY.
  private boolean isCardinalNeighbor(Cell centralCell, Cell neighbor) {
    // Check if the neighbor is directly north, south, east, or west of the central cell
    boolean sameRow = centralCell.getRow() == neighbor.getRow();
    boolean sameCol = centralCell.getCol() == neighbor.getCol();
    boolean adjacentRow = Math.abs(centralCell.getRow() - neighbor.getRow()) == 1;
    boolean adjacentCol = Math.abs(centralCell.getCol() - neighbor.getCol()) == 1;
    return (sameRow && adjacentCol) || (sameCol && adjacentRow);
  }
}

package cellsociety.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Grid<CellType extends Cell> {

  private final int row;
  private final int col;

  private final CellType[][] cellGrid;
  private final Map<CellType, List<CellType>> cellNeighbors;
  private Simulation<CellType> simulation;
  private Stack<String[][]> history;
  private Map<String, Integer> cellCounts;

  /**
   * Constructs a Grid object representing the game board. Initializes a grid of cells and a map for
   * storing neighbors of each cell.
   *
   * @param row The number of rows in the grid.
   * @param col The number of columns in the grid.
   */
  public Grid(int row, int col, char[][] gridState, Simulation<CellType> simulation) {
    this.row = row;
    this.col = col;
    this.simulation = simulation;
    this.cellNeighbors = new HashMap<>();
    this.history = new Stack<String[][]>();
    this.cellGrid = (CellType[][]) new Cell[row][col]; // necessary cast
    initializeGridCells(gridState);
    this.cellCounts = countCellAmount();
  }

  /**
   * Computes the next generation of the grid by applying the given predefined rules. This method
   * iterates over each cell in the current grid, determines its new state based on its neighbors,
   * and updates the cell states in a temporary grid. Once all cells are processed, the main grid is
   * updated with these new states.
   */
  public void computeNextGenerationGrid() {
    // Record history for back button
    recordCurrentGenerationForHistory(cellGrid);

    // First, prepare the next state for each cell
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        CellType cell = cellGrid[i][j];
        List<CellType> neighbors = cellNeighbors.get(cell);
        simulation.prepareCellNextState(cell, neighbors); // Use the simulation logic
        cell.setReadyForNextState(true); // Indicate the cell is ready for its next state
      }
    }

    // Then, apply the next state for all cells that are ready
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        CellType cell = cellGrid[i][j];
        if (cell.isReadyForNextState()) {
          cell.applyNextState();
          cell.setReadyForNextState(false); // Reset the flag
        }
      }
    }

    this.cellCounts = countCellAmount();
  }

  /**
   * Computes the previous generation of the grid. Before computing the next generation, a copy of
   * the cell grid is stored in a stack, called history, and it is popped upon each call to this
   * method, updating the grid in View with this version of the grid.
   */
  public void computePreviousGenerationGrid() {
    if (!history.isEmpty()) {
      String[][] previousStateSnapshot = history.pop();
      for (int i = 0; i < row; i++) {
        for (int j = 0; j < col; j++) {
          cellGrid[i][j].setState(previousStateSnapshot[i][j]);
        }
      }
    }
  }

  /**
   * Retrieves the current state of the cell grid.
   *
   * @return A 2D array (Cell[][]) representing the current grid of cells of type Cell.
   */
  public CellType[][] getCellGrid() {
    return cellGrid;
  }

  private void initializeGridCells(char[][] gridState) {
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        System.out.println(j);
        String state = getStateFromChar(gridState[i][j]);
        CellType currentCell = simulation.createVariationCell(i, j, state);
        cellGrid[i][j] = currentCell;
      }
    }
    buildCellNeighborMap();
  }

  private void buildCellNeighborMap() {
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        cellNeighbors.put(cellGrid[i][j], findCellNeighbors(i, j));
      }
    }
  }

//  private void updateGridWithNewStates(CellType[][] tempGrid) {
//
//  }

  private void recordCurrentGenerationForHistory(CellType[][] currentCellGrid) {
    String[][] stateSnapshot = new String[row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        stateSnapshot[i][j] = cellGrid[i][j].getState();
      }
    }
    // Assuming you have a way to store these snapshots, perhaps as Object if types vary
    history.push(stateSnapshot);
  }


  private void addNeighborsWithinBounds(int newRow, int newCol, List<CellType> neighbors) {
    if (newRow >= 0 && newRow < row && newCol >= 0 && newCol < col) {
      CellType neighbor = cellGrid[newRow][newCol];
      neighbors.add(neighbor);
    }
  }

  private List<CellType> findCellNeighbors(int i, int j) {
    List<CellType> neighbors = new ArrayList<>();
    int[][] directions = {{-1, 0}, {-1, -1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, 1}};
    for (int[] direction : directions) {
      int newRow = i + direction[0];
      int newCol = j + direction[1];
      addNeighborsWithinBounds(newRow, newCol, neighbors);
    }
    return neighbors;
  }

  // Generates the state as a string from the read-in characters
  // .name() returns the string form of the enum constants
  private String getStateFromChar(char cell) {
    String state = "";
    switch (cell) {
      case '0' -> state = CellStates.EMPTY.name();
      case '1' -> state = CellStates.ALIVE.name();
      case '2' -> state = CellStates.DEAD.name();
      case 'T' -> state = CellStates.TREE.name();
      case 'B' -> state = CellStates.BURNING.name();
      case 'X' -> state = CellStates.X.name();
      case 'O' -> state = CellStates.O.name();
      case 'F' -> state = CellStates.FISH.name();
      case 'S' -> state = CellStates.SHARK.name();
      case 'P' -> state = CellStates.PERCOLATED.name();
      case 'W' -> state = CellStates.WALL.name();
      case 'D' -> state = CellStates.SAND.name();
      default -> state = CellStates.ERROR_DETECTED_IN_STATE_NAME.name();
    }
    return state;
  }

  public Map<String, Integer> getCellCounts() {
    return cellCounts;
  }

  private Map<String, Integer> countCellAmount() {
    Map<String, Integer> cellCount = new HashMap<>();
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        CellType cell = cellGrid[i][j];
        cellCount.put(cell.getState(), cellCount.getOrDefault(cell.getState(), 0) + 1);
      }
    }
    return cellCount;
  }
}

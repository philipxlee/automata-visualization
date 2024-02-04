package cellsociety.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid<CellType extends Cell> {

  private final int row;
  private final int col;

  private final CellType[][] cellGrid;
  private final Map<CellType, List<CellType>> cellNeighbors;
  private Simulation<CellType> simulation;

  /**
   * Constructs a Grid object representing the game board. Initializes a grid of cells and a map for
   * storing neighbors of each cell.
   * @param row The number of rows in the grid.
   * @param col The number of columns in the grid.
   */
  public Grid(int row, int col, Simulation<CellType> simulation) {
    this.row = row;
    this.col = col;
    this.simulation = simulation;
    this.cellNeighbors = new HashMap<>();
    this.cellGrid = (CellType[][]) new Cell[row][col]; // cast is necessary
    initializeGridCells();
  }

  /**
   * Computes the next generation of the grid by applying the given predefined rules. This method
   * iterates over each cell in the current grid, determines its new state based on its neighbors,
   * and updates the cell states in a temporary grid. Once all cells are processed, the main grid is
   * updated with these new states.
   */
  public void computeNextGenerationGrid() {
    CellType[][] tempGrid = (CellType[][]) new Cell[row][col]; // necessary cast
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        List<CellType> neighbors = cellNeighbors.get(cellGrid[i][j]);
        String newState = determineNewState(cellGrid[i][j], neighbors);
        tempGrid[i][j] = simulation.createVariationCell(i, j, newState);
      }
    }
    updateGridWithNewStates(tempGrid);
  }

  /**
   * Retrieves the current state of the cell grid.
   * @return A 2D array (Cell[][]) representing the current grid of cells of type Cell.
   */
  public CellType[][] getCellGrid() { return cellGrid; }

  private void initializeGridCells() {
    char[][] gridState = getGridConfiguration();
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
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

  private void updateGridWithNewStates(CellType[][] tempGrid) {
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        String newState = tempGrid[i][j].getState();
        cellGrid[i][j].setState(newState);
      }
    }
  }

  private String determineNewState(CellType cell, List<CellType> neighbors) {
    String currentState = cell.getState();
    return simulation.determineState(cell, currentState, neighbors);
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
      default -> state = CellStates.ERROR_DETECTED_STATE_NAME.name();
    }
    return state;
  }

  /**
   * PLACEHOLDERS
   */

  private char[][] getGridConfiguration() {
    return new char[][]{{'1', '2', '2', '2'}, {'1', '2', '2', '1'}, {'2', '1', '2', '1'}, {'1', '1', '1', '2'}};
  }
}

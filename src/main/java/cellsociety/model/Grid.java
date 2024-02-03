package cellsociety.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid {

  private final int row;
  private final int col;

  private final Cell[][] cellGrid;
  private final Map<Cell, List<Cell>> cellNeighbors;
  private Simulation simulation;

  /**
   * Constructs a Grid object representing the game board. Initializes a grid of cells and a map for
   * storing neighbors of each cell.
   * @param row The number of rows in the grid.
   * @param col The number of columns in the grid.
   */
  public Grid(int row, int col, Simulation simulation) {
    this.row = row;
    this.col = col;
    this.simulation = simulation;
    this.cellNeighbors = new HashMap<>();
    this.cellGrid = new Cell[row][col];
    initializeGridCells();
  }

  /**
   * Computes the next generation of the grid by applying the given predefined rules. This method
   * iterates over each cell in the current grid, determines its new state based on its neighbors,
   * and updates the cell states in a temporary grid. Once all cells are processed, the main grid is
   * updated with these new states.
   */
  public void computeNextGenerationGrid() {
    Cell[][] tempGrid = new Cell[row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        List<Cell> neighbors = cellNeighbors.get(cellGrid[i][j]);
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
  public Cell[][] getCellGrid() { return cellGrid; }

  private void initializeGridCells() {
    char[][] gridState = getGridConfiguration();
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        String state = getStateFromChar(gridState[i][j]);
        Cell currentCell = simulation.createVariationCell(i, j, state);
        cellGrid[i][j] = currentCell;
      }
    }
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        cellNeighbors.put(cellGrid[i][j], findCellNeighbors(i, j));
      }
    }


  }

  private void updateGridWithNewStates(Cell[][] tempGrid) {
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        String newState = tempGrid[i][j].getState();
        cellGrid[i][j].setState(newState);
      }
    }
  }

  private String determineNewState(Cell cell, List<Cell> neighbors) {
    String currentState = cell.getState();
    return simulation.determineState(cell, currentState, neighbors);
  }


  private void addNeighborsWithinBounds(int newRow, int newCol, List<Cell> neighbors) {
    if (newRow >= 0 && newRow < row && newCol >= 0 && newCol < col) {
      Cell neighbor = cellGrid[newRow][newCol];
      neighbors.add(neighbor);
    }
  }

  private List<Cell> findCellNeighbors(int i, int j) {
    List<Cell> neighbors = new ArrayList<>();
    int[][] directions = {{-1, 0}, {-1, -1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, 1}};
    for (int[] direction : directions) {
      int newRow = i + direction[0];
      int newCol = j + direction[1];
      addNeighborsWithinBounds(newRow, newCol, neighbors);
    }
    return neighbors;
  }


  private String getStateFromChar(char cell) {
    String state = "";
    switch (cell) {
      case '0' -> state = "EMPTY";
      case '1' -> state = "ALIVE";
      case '2' -> state = "DEAD";
      case 'T' -> state = "TREE";
      case 'B' -> state = "BURNING";
      case 'X' -> state = "X";
      case 'O' -> state = "O";
      default -> state = "ERROR_DETECTED_IN_GRID";
    }
    return state;
  }

  /**
   * PLACEHOLDERS
   */

  private char[][] getGridConfiguration() {
    return new char[][]{{'1', '1', '2', '2'}, {'1', '1', '2', '2'}, {'2', '2', '1', '1'}, {'2', '2', '1', '1'}};
  }
}

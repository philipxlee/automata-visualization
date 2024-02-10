package cellsociety.model;

import cellsociety.config.Config;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Grid<CellType extends Cell> {

  private final int row;
  private final int col;

  private final CellType[][] cellGrid;
  private final Map<CellType, List<CellType>> cellNeighbors;
  private final Simulation<CellType> simulation;
  private final Stack<String[][]> history;
  private Map<String, Integer> cellCounts;
  private final Deque<CellType> cellDeque = new ArrayDeque<>();

  /**
   * Constructs a Grid object representing the game board. Initializes a grid of cells and a map for
   * storing neighbors of each cell.
   */
  public Grid(Simulation<CellType> simulation, Config config) {
    this.row = config.getHeight();
    this.col = config.getWidth();
    this.simulation = simulation;
    this.cellNeighbors = new HashMap<>();
    this.history = new Stack<String[][]>();
    this.cellGrid = (CellType[][]) new Cell[row][col]; // necessary cast
    initializeGridCells(config);
    this.cellCounts = countCellAmount();
  }

  /**
   * Computes the next generation of the grid. For each cell, the next state is determined based on
   * the current state and the states of its neighbors. Then, the next state is applied to all cells
   * that are ready for it.
   * recordCurrentGenerationForHistory is called to store the current state of the grid in a stack
   * for use in the back button.
   * After the next generation is computed, the cell counts are updated and the grid is
   * converted to a deque for use in the View.
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
    convertCellGridToDeque(cellGrid);
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
    convertCellGridToDeque(cellGrid);
  }


  /**
   * Returns the cellGrid's row
   *
   * @return the row of the cellGrid
   */
  public int getCellRow() {
    return cellGrid.length;
  }

  /**
   * Returns the cellGrid's column
   *
   * @return the column of the cellGrid
   */
  public int getCellCol() {
    return cellGrid[0].length;
  }

  /**
   * Returns each cell in the grid as a stream to be used in the View, preventing direct access to
   * the grid.
   *
   * @return the next cell in the grid
   */
  public CellType getCell() {
    return cellDeque.pop();
  }

  /**
   * Returns the cellCounts map to be used in the View
   *
   * @return the cellCounts map
   */
  public Map<String, Integer> getCellCounts() {
    return cellCounts;
  }

  private void initializeGridCells(Config config) {
    char[][] gridFromConfig = new char[row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        gridFromConfig[i][j] = config.nextCellValue();
      }
    }

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        String state = getStateFromChar(gridFromConfig[i][j]);
        CellType currentCell = simulation.createVariationCell(i, j, state);
        cellGrid[i][j] = currentCell;
      }
    }
    convertCellGridToDeque(cellGrid);
    buildCellNeighborMap();
  }

  private void buildCellNeighborMap() {
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        cellNeighbors.put(cellGrid[i][j], findCellNeighbors(i, j));
      }
    }
  }

  private void recordCurrentGenerationForHistory(CellType[][] currentCellGrid) {
    String[][] stateSnapshot = new String[row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        stateSnapshot[i][j] = cellGrid[i][j].getState();
      }
    }
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
  private String getStateFromChar(char cell) {
    for (CellStates state : CellStates.values()) {
      if (state.getCellChar() == cell) {
        return state.name();
      }
    }
    return CellStates.ERROR_DETECTED_IN_STATE_NAME.name();
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

  private void convertCellGridToDeque(CellType[][] cellGrid) {
    cellDeque.clear();
    for (int i = 0; i < row; i++) {
      cellDeque.addAll(Arrays.asList(cellGrid[i]).subList(0, col));
    }
  }
}

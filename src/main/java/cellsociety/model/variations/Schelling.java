//package cellsociety.model.variations;
//
//import cellsociety.model.CellStates;
//import cellsociety.model.Simulation;
//import cellsociety.model.celltypes.BasicCell;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//
//public class Schelling implements Simulation<BasicCell> {
//
//  private final double THRESHOLD = 0.30;
//  private final String EMPTY = CellStates.EMPTY.name();
//  private final Queue<BasicCell> emptyCells = new LinkedList<>();
//
//  /**
//   * Creates a new SchellingCell with specified row, column, and state.
//   *
//   * @param row   The row position of the cell in the grid.
//   * @param col   The column position of the cell in the grid.
//   * @param state The initial state of the cell, indicating the type of agent.
//   * @return A new instance of SchellingCell with the given parameters.
//   */
//  @Override
//  public BasicCell createVariationCell(int row, int col, String state) {
//    return new BasicCell(row, col, state);
//  }
//
//
//  @Override
//  public String determineState(BasicCell cell, String currentState, List<BasicCell> neighbors) {
//    if (!"X".equals(currentState) && !"O".equals(currentState)) {
//      throw new IllegalStateException("Unexpected cell state: " + currentState);
//    }
//    int[] counts = countNeighborsStates(neighbors);
//    boolean satisfied = calculateSatisfaction(currentState, counts);
//    return satisfied ? currentState : moveToEmptySpaceIfAvailable(currentState);
//  }
//
//  private boolean calculateSatisfaction(String currentState, int[] counts) {
//    int sameStateCount = "X".equals(currentState) ? counts[0] : counts[1];
//    int otherStateCount = "X".equals(currentState) ? counts[1] : counts[0];
//    return (otherStateCount == 0
//        || (double) sameStateCount / (otherStateCount) >= THRESHOLD); // Avoid division by zero
//  }
//
//  private int[] countNeighborsStates(List<BasicCell> neighbors) {
//    int x = 0;
//    int o = 0;
//    for (BasicCell neighbor : neighbors) {
//      int xNeighbor = neighbor.getState().equals("X") ? 1 : 0;
//      int oNeighbor = neighbor.getState().equals("O") ? 1 : 0;
//      x += xNeighbor;
//      o += oNeighbor;
//      if (neighbor.getState().equals(EMPTY)) {
//        emptyCells.add(neighbor);
//      }
//    }
//    return new int[]{x, o};
//  }
//
//  private String moveToEmptySpaceIfAvailable(String cellState) {
//    if (!emptyCells.isEmpty()) {
//      BasicCell emptyCell = emptyCells.poll();
//      emptyCell.setState(cellState);
//      return EMPTY;
//    }
//    return cellState;
//  }
//}

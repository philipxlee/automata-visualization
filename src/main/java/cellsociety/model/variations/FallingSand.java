package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.FallingSandCell;
import java.util.List;

public class FallingSand implements Simulation<FallingSandCell> {

  private final String SAND = CellStates.SAND.name();
  private final String WALL = CellStates.WALL.name();
  private final String EMPTY = CellStates.EMPTY.name();

  @Override
  public FallingSandCell createVariationCell(int row, int col, String state) {
    return new FallingSandCell(row, col, state);
  }

  @Override
  public void prepareCellNextState(FallingSandCell cell, List<FallingSandCell> neighbors) {
    boolean hasSouthNeighbor = false;
    FallingSandCell southNeighbor = null;

    // Directly find the south neighbor in the loop
    for (FallingSandCell neighbor : neighbors) {
      if (hasSouthNeighbor(cell, neighbor)) {
        hasSouthNeighbor = true;
        southNeighbor = neighbor;
        break; // Stop once we find the south neighbor
      }
    }

    // Proceed only if the cell is SAND and it has a south neighbor
    if (cell.getState().equals(SAND) && hasSouthNeighbor) {
      // If south neighbor is empty, the sand can fall
      if (southNeighbor.getState().equals(EMPTY)) {
        cell.setNextState(EMPTY);
        southNeighbor.setNextState(SAND);
      }
      // If south neighbor is a wall or another sand cell, the sand stays
      else if (southNeighbor.getState().equals(WALL) || southNeighbor.getState().equals(SAND)) {
        cell.setNextState(SAND);
        if (southNeighbor.getState().equals(SAND)) {
          southNeighbor.setIsStacked(true);
        }
      }
    }
  }

  private boolean hasSouthNeighbor(FallingSandCell centralCell, FallingSandCell neighbor) {
    boolean sameCol = centralCell.getCol() == neighbor.getCol();
    boolean southRow = neighbor.getRow() == centralCell.getRow() + 1;
    return sameCol && southRow;
  }
}

package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.BasicCell;
import java.util.List;

public class FallingSand implements Simulation<BasicCell> {

  private final String SAND = CellStates.SAND.name();
  private final String WALL = CellStates.WALL.name();
  private final String EMPTY = CellStates.EMPTY.name();

  @Override
  public BasicCell createVariationCell(int row, int col, String state) {
    return new BasicCell(row, col, state);
  }

  @Override
  public void prepareCellNextState(BasicCell cell, List<BasicCell> neighbors) {
    boolean hasSouthNeighbor = false;
    BasicCell southNeighbor = cell;
    for (BasicCell neighbor : neighbors) {
      if (hasSouthNeighbor(cell, neighbor)) {
        hasSouthNeighbor = true;
        southNeighbor = neighbor;
        break;
      }
    }
    handleSandFall(cell, hasSouthNeighbor, southNeighbor);
  }

  private void handleSandFall(BasicCell cell, boolean hasSouthCell, BasicCell southCell) {
    if (cell.getState().equals(SAND) && hasSouthCell) {
      if (southCell.getState().equals(EMPTY)) {
        cell.setNextState(EMPTY);
        southCell.setNextState(SAND);
      } else if (southCell.getState().equals(WALL) || southCell.getState().equals(SAND)) {
        cell.setNextState(SAND);
      }
    }
  }

  private boolean hasSouthNeighbor(BasicCell centralCell, BasicCell neighbor) {
    boolean sameCol = centralCell.getCol() == neighbor.getCol();
    boolean southRow = neighbor.getRow() == centralCell.getRow() + 1;
    return sameCol && southRow;
  }
}

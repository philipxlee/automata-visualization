package cellsociety.model.variations;

import cellsociety.model.CellStates;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.WaTorCell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WaTor implements Simulation<WaTorCell> {

  private final String EMPTY = CellStates.EMPTY.name();
  private final String FISH = CellStates.FISH.name();
  private final String SHARK = CellStates.SHARK.name();
  private List<WaTorCell> emptyCells = new ArrayList<>();
  private Map<WaTorCell, WaTorCell> intendedMoves = new HashMap<>();
  private Random rand = new Random();

  @Override
  public WaTorCell createVariationCell(int row, int col, String state) {
    WaTorCell cell = new WaTorCell(row, col, state);
    if (state.equals(EMPTY)) {
      emptyCells.add(cell);
    }
    return cell;
  }

  @Override
  public void prepareCellNextState(WaTorCell cell, List<WaTorCell> neighbors) {
    // This method will now only decide on moves, not execute them immediately
    String currentState = cell.getState();
    if (!currentState.equals(EMPTY)) {
      List<WaTorCell> validTargets = new ArrayList<>();
      if (currentState.equals(FISH)) {
        validTargets = findSpecificNeighbors(neighbors, EMPTY);
      } else if (currentState.equals(SHARK)) {
        validTargets.addAll(findSpecificNeighbors(neighbors, FISH)); // Prefer fish
        if (validTargets.isEmpty()) {
          validTargets.addAll(findSpecificNeighbors(neighbors, EMPTY)); // Then empty if no fish
        }
      }

      // Decide on move
      if (!validTargets.isEmpty()) {
        Collections.shuffle(validTargets); // Randomize to simulate random selection
        WaTorCell target = validTargets.get(0); // Take the first after shuffling
        intendedMoves.put(cell, target); // Map current cell to target for move execution later
      }
    }
  }

  public void executeMoves() {
    // Execute moves based on intendedMoves map
    for (Map.Entry<WaTorCell, WaTorCell> entry : intendedMoves.entrySet()) {
      WaTorCell source = entry.getKey();
      WaTorCell target = entry.getValue();

      if (source.getState().equals(FISH) && target.getState().equals(EMPTY)) {
        performMove(source, target, FISH);
      } else if (source.getState().equals(SHARK)) {
        if (target.getState().equals(FISH) || target.getState().equals(EMPTY)) {
          performMove(source, target, SHARK);
        }
      }
    }
    intendedMoves.clear(); // Clear moves for next round
  }

  private void performMove(WaTorCell source, WaTorCell target, String newState) {
    target.setNextState(newState);
    source.setNextState(EMPTY);
    // Update emptyCells list
    if (!newState.equals(EMPTY)) {
      emptyCells.remove(target); // Target is no longer empty
      emptyCells.add(source); // Source becomes empty
    }
    // Further adjustments for energy, reproduction, etc., should be handled here
  }

  private List<WaTorCell> findSpecificNeighbors(List<WaTorCell> neighbors, String state) {
    List<WaTorCell> specificNeighbors = new ArrayList<>();
    for (WaTorCell neighbor : neighbors) {
      if (neighbor.getState().equals(state)) {
        specificNeighbors.add(neighbor);
      }
    }
    return specificNeighbors;
  }
}

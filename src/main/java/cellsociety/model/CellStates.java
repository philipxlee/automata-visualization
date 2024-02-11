package cellsociety.model;

/**
 * Enum representing the possible states of a cell in a simulation.
 */
public enum CellStates {
  EMPTY('0'),
  ALIVE('1'),
  DEAD('2'),
  TREE('T'),
  BURNING('B'),
  X('X'),
  O('O'),
  FISH('F'),
  SHARK('S'),
  PERCOLATED('P'),
  WALL('W'),
  SAND('D'),
  ANT('A'),
  VISITED('V'),
  ERROR_DETECTED_IN_STATE_NAME('E');

  private final char cellChar;

  CellStates(char cellChar) {
    this.cellChar = cellChar;
  }

  /**
   * Get the character representation of the cell state.
   *
   * @return The character representation of the cell state.
   */
  public char getCellChar() {
    return cellChar;
  }
}
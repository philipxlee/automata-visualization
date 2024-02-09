package cellsociety.model;

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

  CellStates(char cellChar) {
    this.cellChar = cellChar;
  }

  public char getCellChar() {
    return cellChar;
  }

  private final char cellChar;
}
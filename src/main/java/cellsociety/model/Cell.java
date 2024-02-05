package cellsociety.model;

public abstract class Cell {

  private final int row;
  private final int col;
  private String state;


  public Cell(int row, int col, String state) {
    this.row = row;
    this.col = col;
    this.state = state;
  }

  public int getRow() {
    return this.row;
  }

  public int getCol() {
    return this.col;
  }

  public String getState() {
    return this.state;
  }

  public void setState(String state) {
    this.state = state;
  }
}

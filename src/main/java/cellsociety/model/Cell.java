package cellsociety.model;

public abstract class Cell {

  private static final String NO_NEXT_STATE = "";
  private final int row;
  private final int col;
  private boolean readyForNextState = false;
  private String state;
  private String nextState;

  public Cell(int row, int col, String state) {
    this.row = row;
    this.col = col;
    this.state = state;
  }

  public void applyNextState() {
    if (nextState != null) {
      setState(nextState);
      nextState = null; // Clear the next state after applying it
    }
  }

  public boolean isReadyForNextState() {
    return readyForNextState;
  }

  public void setReadyForNextState(boolean ready) {
    this.readyForNextState = ready;
  }

  public int getRow() {
    return this.row;
  }

  public int getCol() {
    return this.col;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setNextState(String state) {
    this.nextState = state;
  }

  public String getState() {
    return this.state;
  }
}

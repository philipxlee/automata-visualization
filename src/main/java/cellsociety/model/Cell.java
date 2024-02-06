package cellsociety.model;

public abstract class Cell {

  private final int row;
  private final int col;
  private String state;
  private String nextState;

  public Cell(int row, int col, String state) {
    this.row = row;
    this.col = col;
    this.state = state;
  }

  // A flag to indicate if the cell's next state is ready to be applied
  private boolean readyForNextState = false;

  public boolean isReadyForNextState() {
    return readyForNextState;
  }

  public void setReadyForNextState(boolean ready) {
    this.readyForNextState = ready;
  }

  public void applyNextState() {
    if (nextState != null) {
      setState(nextState);
      nextState = null; // Clear the next state after applying it
    }
  }

  // Method to set the next state, assuming nextState is managed in this class
  public void setNextState(String state) {
    this.nextState = state;
  }

  public String getNextState() {
    return nextState;
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

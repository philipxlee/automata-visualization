package cellsociety.model;

public abstract class Cell {

  private final int row;
  private final int col;

  private boolean readyForNextState = false;
  private String state;
  private String nextState;

  /**
   * Create a new cell with the given state at the given row and column
   *
   * @param row   The row of the cell
   * @param col   The column of the cell
   * @param state The state of the cell
   */
  public Cell(int row, int col, String state) {
    this.row = row;
    this.col = col;
    this.state = state;
  }

  /**
   * Apply the next state to the cell
   */
  public void applyNextState() {
    if (nextState != null) {
      setState(nextState);
      nextState = null; // Clear the next state after applying it
    }
  }

  /**
   * Check if the cell is ready to move to the next state
   *
   * @return true if the cell is ready to move to the next state, false otherwise
   */
  public boolean isReadyForNextState() {
    return readyForNextState;
  }

  /**
   * Set whether the cell is ready to move to the next state
   *
   * @param ready true if the cell is ready to move to the next state, false otherwise
   */
  public void setReadyForNextState(boolean ready) {
    this.readyForNextState = ready;
  }

  /**
   * Get the row of the cell
   *
   * @return the row of the cell
   */
  public int getRow() {
    return this.row;
  }

  /**
   * Get the column of the cell
   *
   * @return the column of the cell
   */
  public int getCol() {
    return this.col;
  }

  /**
   * Get the state of the cell
   *
   * @return the state of the cell
   */
  public void setNextState(String state) {
    this.nextState = state;
  }

  /**
   * Get the state of the cell
   *
   * @return the state of the cell
   */
  public String getState() {
    return this.state;
  }

  /**
   * Set the state of the cell
   *
   * @param state the state of the cell
   */
  public void setState(String state) {
    this.state = state;
  }
}

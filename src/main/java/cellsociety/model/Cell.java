package cellsociety.model;

public class Cell {
    private final int ROW;
    private final int COL;
    private String state;

    public Cell (int row, int col, String state) {
        this.ROW = row;
        this.COL = col;
        this.state = state;
    }

    public void setState(String state) { this.state = state; }
    public String getState() { return this.state; }

}

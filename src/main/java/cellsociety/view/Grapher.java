package cellsociety.view;

import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Grapher {

  private static final int GRAPHER_WIDTH = 600;
  private static final int GRAPHER_HEIGHT = 500;
  private LineChart<Number, Number> myLineChart;
  private Map<String, XYChart.Series<Number, Number>> seriesMap;
  private int currentTick;
  private int maxTick;
  private Stage myStage;
  private Scene myScene;

  public Grapher(Stage stage) {
    this.myStage = stage;
    this.currentTick = 0;

    NumberAxis chartAxisX = new NumberAxis();
    chartAxisX.setLabel("Ticks");
    NumberAxis chartAxisY = new NumberAxis();
    chartAxisY.setLabel("Population");

    // Creating the line chart
    this.myLineChart = new LineChart<>(chartAxisX, chartAxisY);
    this.seriesMap = new HashMap<>();
    this.myLineChart.setLegendSide(Side.TOP);
    this.myScene = new Scene(myLineChart, GRAPHER_WIDTH, GRAPHER_HEIGHT);
    this.myScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    this.myStage.setScene(myScene);
  }

  /**
   * Shows the grapher
   */
  public void show() {
    myStage.show();
  }

  /**
   * Adds a data point to the grapher
   *
   * @param dataPoint the data point to add
   * @param tick      the tick at which the data point was recorded
   */
  public void addData(Map<String, Integer> dataPoint, int tick) {
    if (tick <= maxTick) {
      currentTick = tick;
      return;
    }
    for (Map.Entry<String, Integer> entry : dataPoint.entrySet()) {
      String seriesName = entry.getKey();
      if (seriesName.equals("EMPTY")) {
        continue;
      }
      XYChart.Series<Number, Number> series = seriesMap.get(seriesName);
      if (series == null) {
        series = new XYChart.Series<>();
        series.setName(seriesName);
        myLineChart.getData().add(series);
        seriesMap.put(seriesName, series);
      }
      series.getData().add(new XYChart.Data<>(tick, entry.getValue()));
    }
    currentTick = tick;
    maxTick = currentTick;
  }

  /**
   * Closes the grapher
   */
  public void close() {
    myStage.close();
  }

  /**
   * @return true if the grapher is showing
   */
  public boolean isShowing() {
    return myStage.isShowing();
  }

  /**
   * @return the current tick
   */
  public int getTick() {
    return currentTick;
  }

  /**
   * @param tick the tick to set
   */
  public void setTick(int tick) {
    currentTick = tick;
  }
}
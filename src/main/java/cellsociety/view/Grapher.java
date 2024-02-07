package cellsociety.view;

import cellsociety.model.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Grapher {
  private List<Map<String, Integer>> cellCounts;
  private LineChart<Number, Number> myLineChart;
  private Stage myStage;
  private Scene myScene;
  public Grapher(Stage stage) {
    this.myStage = stage;
    this.cellCounts = new ArrayList<>();

    NumberAxis xAxis = new NumberAxis();
    xAxis.setLabel("Ticks");
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Population");

    // Creating the line chart
    this.myLineChart = new LineChart<>(xAxis, yAxis);
    this.myLineChart.setTitle("Cell Population Over Time");
    this.myScene = new Scene(myLineChart, 600, 400);
    myScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    myStage.setScene(myScene);
  }

  public void show() {
    // Creating a scene object
    updateGraph();

    // Displaying the contents of the stage
    myStage.show();
  }

  public void updateGraph(){
//    myLineChart.getData().clear();
    // Iterate over cellCounts
    for (int i = 0; i < cellCounts.size(); i++) {
      Map<String, Integer> cellCount = cellCounts.get(i);
      for (Map.Entry<String, Integer> entry : cellCount.entrySet()) {
        String seriesName = entry.getKey();
        Integer count = entry.getValue();

        // Check if series already exists
        XYChart.Series<Number, Number> series = myLineChart.getData().stream()
            .filter(s -> s.getName().equals(seriesName))
            .findFirst()
            .orElse(null);

        if (series == null) {
          // If series does not exist, create it and add to chart
          series = new XYChart.Series<>();
          series.setName(seriesName);
          myLineChart.getData().add(series);
        }

        // Add data to series
        series.getData().add(new XYChart.Data<>(i, count));
      }
    }
  }


  public void updateData(List<Map<String, Integer>> cellCounts) {
    this.cellCounts.clear();
    this.cellCounts.addAll(cellCounts);
  }

  public void close() {
    myStage.close();
  }

  public boolean isShowing(){
    return myStage.isShowing();
  }
}
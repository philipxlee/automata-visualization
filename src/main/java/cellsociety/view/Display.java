package cellsociety.view;

import cellsociety.config.Config;
import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;


public class Display {

  private static final String language = "English";
  public String DEFAULT_RESOURCE_PACKAGE = "cellsociety.view.";
  public String DEFAULT_RESOURCE_FOLDER = "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/");
  public String STYLESHEET = "styles.css";
  private int WINDOW_WIDTH = 1024;
  private int WINDOW_HEIGHT = 768;
  //region Temporary hard-coded values
  private String simType;
  private String author;
  private String description;
  private Map<String, Color> stateColors = Map.of(
      "ALIVE", Color.BLACK,
      "EMPTY", Color.WHITE,
      "PERCOLATED", Color.BLUE,
      "WALL", Color.BLACK,
      "BURNING", Color.RED,
      "TREE", Color.GREEN,
      "FISH", Color.ORANGE,
      "SHARK", Color.DARKGRAY
  );
  private Map<String, Double> parameterValues;
  //endregion
  private Stage primaryStage;
  private Grapher myGrapher;
  private Timeline myTimeline;
  private Grid<Cell> simulationGrid;
  private ResourceBundle resources;
  private BorderPane root;


  public Display(Stage primaryStage, Grid grid, Config config) {
    this.primaryStage = primaryStage;
    this.simulationGrid = grid;
    this.parameterValues = config.getParameters();
    this.simType = config.getSimulationTextInfo()[0];
    this.author = config.getSimulationTextInfo()[2];
    this.description = config.getSimulationTextInfo()[3];

  }

  public void start() {
    resources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
    //make simulation class with the info passed from config
    showScene();
    primaryStage.setTitle(resources.getString("title"));

    Stage graphStage = new Stage();
    graphStage.setTitle("Cell Population Over Time");
    this.myGrapher = new Grapher(graphStage);
    this.myGrapher.updateData(simulationGrid.getCellCounts());

    this.myTimeline = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> nextTick()));
    myTimeline.setCycleCount(Timeline.INDEFINITE);
  }

  private void showScene() {
    Scene scene = createScene();
    scene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private Scene createScene() {
    root = new BorderPane();

    updateGrid();

    VBox mainUI = createMainUI();
    root.setRight(mainUI);

    return new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
  }

  private void updateGrid() {
    Pane gridSection = new Pane();
    gridSection.getStyleClass().add("grid");
    createGridUI(gridSection);
    root.setCenter(gridSection);
  }

  private void createGridUI(Pane gridSection) {
    Cell[][] grid = simulationGrid.getCellGrid();

    double cellWidth = (double) (WINDOW_HEIGHT) / grid[0].length;
    double cellHeight = (double) WINDOW_HEIGHT / grid.length;

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        Rectangle cell = new Rectangle(cellWidth, cellHeight);
        cell.getStyleClass().add("cell");
        cell.setStrokeWidth(calculateStrokeWidth(grid.length));
        cell.setX(j * cellWidth);
        cell.setY(i * cellHeight);
        String state = grid[i][j].getState();
        Color color = stateColors.get(state);
        if (color != null) {
          cell.setFill(color);
        } else {
          cell.setFill(Color.TRANSPARENT);
        }
        gridSection.getChildren().add(cell);
      }
    }
  }

  private VBox createMainUI() {
    VBox newUI = new VBox();
    newUI.setPrefWidth(WINDOW_WIDTH - WINDOW_HEIGHT);
    newUI.getStyleClass().add("ui");

    VBox infoPane = new VBox();
    infoPane.setPrefWidth(WINDOW_WIDTH - WINDOW_HEIGHT);
    infoPane.setPrefHeight(WINDOW_HEIGHT / 2);
    createDisplayUI(infoPane);
    newUI.getChildren().add(infoPane);

    Separator separator = new Separator();
    newUI.getChildren().add(separator);

    VBox controlPane = new VBox();
    controlPane.setPrefWidth(WINDOW_WIDTH - WINDOW_HEIGHT);
    controlPane.setPrefHeight(WINDOW_HEIGHT / 2);
    createControlUI(controlPane);
    newUI.getChildren().add(controlPane);

    return newUI;
  }

  private void createDisplayUI(VBox infoPane) {
    Label simLabel = new Label(simType);
    simLabel.getStyleClass().add("title");
    infoPane.getChildren().add(simLabel);

    Label authorLabel = new Label(resources.getString("author") + ": " + author);
    infoPane.getChildren().add(authorLabel);

    Label descriptionLabel = new Label(description);
    descriptionLabel.getStyleClass().add("description");
    infoPane.getChildren().add(descriptionLabel);

    for (Map.Entry<String, Color> entry : stateColors.entrySet()) {
      Label stateColorLabel = new Label(resources.getString("state") + ": " + entry.getKey());
      stateColorLabel.getStyleClass().add("states");
      stateColorLabel.setTextFill(entry.getValue());
      infoPane.getChildren().add(stateColorLabel);
    }

    for (Map.Entry<String, Double> entry : parameterValues.entrySet()) {
      Label parameterValueLabel = new Label("• " + entry.getKey() + ": " + entry.getValue());
      parameterValueLabel.getStyleClass().add("params");
      infoPane.getChildren().add(parameterValueLabel);
    }

    infoPane.setAlignment(Pos.BASELINE_CENTER);
  }

  private void nextTick() {
    simulationGrid.computeNextGenerationGrid();
    updateGrid();
    myGrapher.updateData(simulationGrid.getCellCounts());
    myGrapher.updateGraph();
  }

  private void createControlUI(VBox controlPane) {
    HBox row1 = new HBox();
    Button playButton = makeButton("PlayCommand", event -> {
      if (myTimeline.getStatus().equals(Timeline.Status.RUNNING)) {
        return;
      } else if (myTimeline.getStatus().equals(Timeline.Status.PAUSED) || myTimeline.getStatus()
          .equals(Timeline.Status.STOPPED)) {
        myTimeline.play();
      }
    });
    Button pauseButton = makeButton("PauseCommand", event -> myTimeline.pause());

    row1.getChildren().add(playButton);
    row1.getChildren().add(pauseButton);
    row1.setAlignment(Pos.BASELINE_CENTER);
    controlPane.getChildren().add(row1);

    HBox row2 = new HBox();
    Button nextButton = makeButton("NextCommand", event -> nextTick());
    Button backButton = makeButton("BackCommand", event -> {
      simulationGrid.computePreviousGenerationGrid();
      updateGrid();
    });
    row2.getChildren().add(nextButton);
    row2.getChildren().add(backButton);
    row2.setAlignment(Pos.BASELINE_CENTER);
    controlPane.getChildren().add(row2);

    HBox row3 = new HBox();
    Button toggleGrapher = makeButton("GraphCommand", event -> {
      if (myGrapher.isShowing()) {
        myGrapher.close();
      } else {
        myGrapher.show();
      }
    });
    row3.setAlignment(Pos.BASELINE_CENTER);

    row3.getChildren().add(toggleGrapher);
    controlPane.getChildren().add(row3);

    controlPane.setAlignment(Pos.BASELINE_CENTER);
  }

  private Button makeButton(String property, EventHandler<ActionEvent> handler) {
    final String IMAGE_FILE_SUFFIXES = String.format(".*\\.(%s)",
        String.join("|", ImageIO.getReaderFileSuffixes()));
    Button result = new Button();
    String label = resources.getString(property);
    if (label.matches(IMAGE_FILE_SUFFIXES)) {
      result.setGraphic(new ImageView(
          new Image(getClass().getResourceAsStream(DEFAULT_RESOURCE_FOLDER + label))));
    } else {
      result.setText(label);
    }
    result.setOnAction(handler);
    return result;
  }

  private double calculateStrokeWidth(double gridSize) {
    return gridSize <= 10 ? 5 : 50 / gridSize;
  }
}
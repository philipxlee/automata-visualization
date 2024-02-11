package cellsociety.view;

import cellsociety.config.Config;
import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;


public class Display {

  public String DEFAULT_RESOURCE_PACKAGE = "cellsociety.view.";
  public String DEFAULT_RESOURCE_FOLDER = "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/");
  public String STYLESHEET = "styles.css";
  private int WINDOW_WIDTH = 1024;
  private int WINDOW_HEIGHT = 768;
  //region Temporary hard-coded values
  private String simType;
  private String author;
  private String description;
  private Map<String, Color> stateColors = Map.ofEntries(
      new SimpleEntry<>("ALIVE", Color.BLACK),
      new SimpleEntry<>("EMPTY", Color.WHITE),
      new SimpleEntry<>("PERCOLATED", Color.BLUE),
      new SimpleEntry<>("WALL", Color.BLACK),
      new SimpleEntry<>("BURNING", Color.RED),
      new SimpleEntry<>("TREE", Color.GREEN),
      new SimpleEntry<>("FISH", Color.ORANGE),
      new SimpleEntry<>("SHARK", Color.DARKGRAY),
      new SimpleEntry<>("X", Color.RED),
      new SimpleEntry<>("O", Color.BLUE),
      new SimpleEntry<>("SAND", Color.PEACHPUFF),
      new SimpleEntry<>("ANT", Color.SADDLEBROWN),
      new SimpleEntry<>("VISITED", Color.DARKBLUE)
      // Add more entries as needed
  );
  private Map<String, Double> parameterValues;
  //endregion
  private Stage primaryStage;
  private Grapher myGrapher;
  private Config myConfig;
  private Timeline myTimeline;
  private Grid<Cell> simulationGrid;
  private ResourceBundle resources;
  private BorderPane root;
  private String myLanguage;
  private boolean gridOutline;


  public Display(Stage primaryStage, Grid grid, Config config) {
    this.primaryStage = primaryStage;
    this.simulationGrid = grid;
    this.myConfig = config;
    this.parameterValues = config.getParameters();
    this.simType = config.getSimulationTextInfo()[0];
    this.author = config.getSimulationTextInfo()[2];
    this.description = config.getSimulationTextInfo()[3];
    this.myLanguage = config.getLanguage();
    this.gridOutline = true;
  }

  public void start() {
    resources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + myLanguage);
    //make simulation class with the info passed from config
    showScene();
    primaryStage.setTitle(resources.getString("title"));

    Stage graphStage = new Stage();
    graphStage.setTitle(resources.getString("GraphTitle"));
    this.myGrapher = new Grapher(graphStage);
    this.myGrapher.addData(simulationGrid.getCellCounts(), 0);

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
//    Cell[][] grid = simulationGrid.getCellGrid();
    Cell[][] grid = getCellGrid();

    double cellWidth = (double) (WINDOW_HEIGHT) / grid[0].length;
    double cellHeight = (double) WINDOW_HEIGHT / grid.length;

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        Rectangle cell = new Rectangle(cellWidth, cellHeight);
        cell.getStyleClass().add(gridOutline ? "cell-outlined" : "cell-no-outline");
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
    controlPane.getStyleClass().add("control-pane");
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
    myGrapher.addData(simulationGrid.getCellCounts(), myGrapher.getTick() + 1);
  }

  private void lastTick() {
    simulationGrid.computePreviousGenerationGrid();
    updateGrid();
    myGrapher.setTick(myGrapher.getTick() - 1);
  }

  private void toggleGrapher() {
    if (myGrapher.isShowing()) {
      myGrapher.close();
    } else {
      myGrapher.show();
    }
  }

  private Cell[][] getCellGrid() {
    Cell[][] grid = new Cell[simulationGrid.getCellRow()][simulationGrid.getCellCol()];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        grid[i][j] = simulationGrid.getCell();
      }
    }
    return grid;
  }

  private void createControlUI(VBox controlPane) {
    HBox row1 = new HBox();
    row1.getStyleClass().add("button-row");
    Button playButton = makeButton("PlayCommand", event -> myTimeline.play());
    Button pauseButton = makeButton("PauseCommand", event -> myTimeline.pause());


    HBox row2 = new HBox();
    row2.getStyleClass().add("button-row");
    Button nextButton = makeButton("NextCommand", event -> nextTick());
    Button backButton = makeButton("BackCommand", event -> lastTick());

    HBox row3 = new HBox();
    row3.getStyleClass().add("button-row");

    Label sliderLabel = new Label(resources.getString("SpeedSlider"));
    Slider speedSlider = new Slider();
    speedSlider.setMin(0);
    speedSlider.setMax(100);
    speedSlider.setValue(10);
    speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      myTimeline.setRate(newValue.doubleValue() / 10);
    });
    VBox sliderBox = new VBox(sliderLabel, speedSlider);
    sliderBox.getStyleClass().add("dropdown");

    Label comboBoxLabel = new Label(resources.getString("EdgePolicy"));
    ComboBox<String> comboBox = new ComboBox<>();
    comboBox.getItems().addAll("Plane", "Toroidal"); // LANGUAGE CONSIDERATION
    comboBox.setValue("Plane");
    VBox comboEdgePolicy = new VBox(comboBoxLabel, comboBox);
    comboEdgePolicy.getStyleClass().add("dropdown");

    HBox row4 = new HBox();
    row4.getStyleClass().add("button-row");
    Button saveButton = makeButton("SaveCommand", event -> {
//      myConfig.saveXmlFile("savedFile", getCellGrid());
    });


    CheckBox toggleGrapher = new CheckBox(resources.getString("GraphCommand"));
    toggleGrapher.setOnAction(event -> toggleGrapher());
    CheckBox toggleOutline = new CheckBox(resources.getString("OutlineCommand"));
    toggleOutline.setSelected(true);
    toggleOutline.setOnAction(event -> {
      if (toggleOutline.isSelected()) {
        gridOutline = true;
        updateCurrentGrid();
      } else {
        gridOutline = false;
        updateCurrentGrid();
      }
    });

    HBox row5 = new HBox();
    row5.getStyleClass().add("button-row");

    row1.getChildren().add(playButton);
    row1.getChildren().add(pauseButton);
    row2.getChildren().add(nextButton);
    row2.getChildren().add(backButton);
    row3.getChildren().add(sliderBox);
    row4.getChildren().add(toggleGrapher);
    row4.getChildren().add(toggleOutline);
    row5.getChildren().add(comboEdgePolicy);
    row5.getChildren().add(saveButton);

    controlPane.getChildren().add(row1);
    controlPane.getChildren().add(row2);
    controlPane.getChildren().add(row3);
    controlPane.getChildren().add(row4);
    controlPane.getChildren().add(row5);

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

  private void updateCurrentGrid() {
    simulationGrid.computeNextGenerationGrid();
    simulationGrid.computePreviousGenerationGrid();
    updateGrid();
  }
}
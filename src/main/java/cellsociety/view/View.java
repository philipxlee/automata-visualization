package cellsociety.view;

import cellsociety.model.Cell;
import cellsociety.model.Grid;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.ResourceBundle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javax.imageio.ImageIO;


public class View {

  private static final int WINDOW_WIDTH = 1024;
  private static final int WINDOW_HEIGHT = 768;
  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.view.";
  public static final String DEFAULT_RESOURCE_FOLDER = "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/");
  public static final String STYLESHEET = "styles.css";


  //region Temporary hard-coded values
  private static final String simType = "Game of Life";
  private static final String author = "John Conway";
  private static final String description = "The Game of Life is a cellular automaton devised by the British mathematician John Horton Conway in 1970. It is a zero-player game, meaning that its evolution is determined by its initial state, requiring no further input. One interacts with the Game of Life by creating an initial configuration and observing how it evolves.";
  private static final Map<String, Color> stateColors = Map.of(
    "ALIVE", Color.BLACK,
    "DEAD", Color.WHITE
  );
  private static final Map<String, Double> parameterValues = Map.of(
      "probCatch",0.5,
      "randomParam", 0.99,
      "randomParam2", 0.21
  );
  private static final String language = "English";
  //endregion

  private Stage primaryStage;
  private Grid simulationGrid;
  private ResourceBundle resources;
  private Button playButton;
  private Button pauseButton;
  private Button nextButton;
  private Button backButton;
  private BorderPane root;


  public View(Stage primaryStage, Grid grid) {
    this.primaryStage = primaryStage;
    this.simulationGrid = grid;
  }

  public void start() {
    resources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
    //make simulation class with the info passed from config
    showScene();
    primaryStage.setTitle(resources.getString("title"));
  }

  private void showScene() {
    Scene scene = createScene();
    scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
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

  private void createControlUI(VBox controlPane) {
    HBox row1 = new HBox();
    playButton = makeButton("PlayCommand", null);
    pauseButton = makeButton("PauseCommand", null);

    row1.getChildren().add(playButton);
    row1.getChildren().add(pauseButton);
    row1.setAlignment(Pos.BASELINE_CENTER);
    controlPane.getChildren().add(row1);

    HBox row2 = new HBox();
    nextButton = makeButton("NextCommand", event -> {
      simulationGrid.computeNextGenerationGrid();
      updateGrid();
    });
    backButton = makeButton("BackCommand", null);
    row2.getChildren().add(nextButton);
    row2.getChildren().add(backButton);
    row2.setAlignment(Pos.BASELINE_CENTER);
    controlPane.getChildren().add(row2);

    controlPane.setAlignment(Pos.BASELINE_CENTER);
  }

  private Button makeButton (String property, EventHandler<ActionEvent> handler) {
    final String IMAGE_FILE_SUFFIXES = String.format(".*\\.(%s)", String.join("|", ImageIO.getReaderFileSuffixes()));
    Button result = new Button();
    String label = resources.getString(property);
    if (label.matches(IMAGE_FILE_SUFFIXES)) {
      result.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(DEFAULT_RESOURCE_FOLDER + label))));
    }
    else {
      result.setText(label);
    }
    result.setOnAction(handler);
    return result;
  }
}
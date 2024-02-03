package cellsociety.view;

import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.ResourceBundle;
import javafx.stage.Stage;


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
    "Live", Color.WHITE,
    "Dead", Color.BLACK
  );
  private static final Map<String, Double> parameterValues = Map.of(
      "probCatch",0.5,
      "randomParam", 0.99,
      "randomParam2", 0.21
  );
  private static final String language = "Spanish";
  //endregion

  private Stage primaryStage;
  private ResourceBundle myResources;


  public View(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void start() {
    myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
    showScene();
    primaryStage.setTitle(myResources.getString("title"));
  }

  private void showScene() {
    Scene scene = createScene();
    scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private Scene createScene() {
    BorderPane root = new BorderPane();

    Pane gridSection = new Pane();
    gridSection.setBackground(new Background(new BackgroundFill(Color.BLANCHEDALMOND, null, null)));
    root.setCenter(gridSection);

    VBox mainUI = createMainUI();
    root.setRight(mainUI);

    return new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
  }

  private VBox createMainUI() {
    VBox newUI = new VBox();
    newUI.setPrefWidth(WINDOW_WIDTH - WINDOW_HEIGHT);
    newUI.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

    VBox infoPane = new VBox();
    infoPane.setPrefWidth(WINDOW_WIDTH - WINDOW_HEIGHT);
    infoPane.setPrefHeight(WINDOW_HEIGHT / 2);
    createDisplayUI(infoPane);
    newUI.getChildren().add(0, infoPane);

    return newUI;
  }

  private void createDisplayUI(VBox infoPane) {
    Label simLabel = new Label(simType);
    simLabel.getStyleClass().add("title");
    infoPane.getChildren().add(simLabel);

    Label authorLabel = new Label(myResources.getString("author") + ": " + author);
    infoPane.getChildren().add(authorLabel);

    Label descriptionLabel = new Label(description);
    descriptionLabel.getStyleClass().add("description");
    infoPane.getChildren().add(descriptionLabel);

    for (Map.Entry<String, Color> entry : stateColors.entrySet()) {
      Label stateColorLabel = new Label(myResources.getString("state") + ": " + entry.getKey());
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

}
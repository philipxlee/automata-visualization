package cellsociety.view;

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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.ResourceBundle;
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
    "Live", Color.WHITE,
    "Dead", Color.BLACK
  );
  private static final Map<String, Double> parameterValues = Map.of(
      "probCatch",0.5,
      "randomParam", 0.99,
      "randomParam2", 0.21
  );
  private static final String language = "English";
  //endregion

  private Stage primaryStage;
  private ResourceBundle resources;
  private Button playButton;
  private Button pauseButton;


  public View(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void start() {
    resources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
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
    BorderPane root = new BorderPane();

    Pane gridSection = new Pane();
    gridSection.getStyleClass().add("grid");
    root.setCenter(gridSection);

    VBox mainUI = createMainUI();
    root.setRight(mainUI);

    return new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
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
    playButton = makeButton("PlayCommand", null);
    controlPane.getChildren().add(playButton);

    pauseButton = makeButton("PauseCommand", null);
    controlPane.getChildren().add(pauseButton);

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
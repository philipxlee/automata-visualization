package cellsociety.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class View {

  private static final int WINDOW_WIDTH = 1024;
  private static final int WINDOW_HEIGHT = 768;

  //region Temporary hard-coded values
  private static final String simType = "Game of Life";
  private static final String author = "John Conway";
  private static final String description = "The Game of Life is a cellular automaton devised by the British mathematician John Horton Conway in 1970. It is a zero-player game, meaning that its evolution is determined by its initial state, requiring no further input. One interacts with the Game of Life by creating an initial configuration and observing how it evolves.";
  //endregion

  private Stage primaryStage;

  public View(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void start() {
    primaryStage.setTitle("Cell Society");
    showScene();
  }

  private void showScene() {
    Scene scene = createScene();
    scene.getStylesheets().add(getClass().getResource("/cellsociety/view/styles.css").toExternalForm());
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
    Label simLabel = createLabel(simType);
    simLabel.getStyleClass().add("label-title");
    infoPane.getChildren().add(simLabel);

    Label authorLabel = createLabel("Author: " + author);
    infoPane.getChildren().add(authorLabel);

    Label descriptionLabel = createLabel(description);
    descriptionLabel.getStyleClass().add("label-description");
    infoPane.getChildren().add(descriptionLabel);

    infoPane.setAlignment(Pos.BASELINE_CENTER);
  }

  private Label createLabel(String text) {
    Label label = new Label(text);
    return label;
  }

}
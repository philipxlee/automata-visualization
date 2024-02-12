package cellsociety;

import cellsociety.config.Config;
import cellsociety.config.Control;
import cellsociety.model.Cell;
import cellsociety.model.Grid;
import cellsociety.model.Simulation;
import cellsociety.model.variations.FallingSand;
import cellsociety.model.variations.ForagingAnts;
import cellsociety.model.variations.GameOfLife;
import cellsociety.model.variations.Percolation;
import cellsociety.model.variations.Schelling;
import cellsociety.model.variations.SpreadingOfFire;
import cellsociety.model.variations.WaTor;
import cellsociety.view.Display;
import java.io.File;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * Feel free to completely change this code or delete it entirely.
 */
public class Main extends Application {


  /**
   * Start the program, give complete Control to JavaFX.
   * <p>
   * Default version of main() is actually included within JavaFX, so this is not technically
   * necessary!
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * @see Application#start(Stage)
   */
  @Override
  public void start(Stage primaryStage) {
    Control control = new Control();
    control.makeSimulation(primaryStage);
    control.showMessage(AlertType.INFORMATION, "Press N to start another simulation");
  }

}

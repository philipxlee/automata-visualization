package cellsociety;

import cellsociety.config.Control;
import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
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

package cellsociety;

import cellsociety.config.Control;
import javafx.application.Application;
import javafx.stage.Stage;


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
  }

}

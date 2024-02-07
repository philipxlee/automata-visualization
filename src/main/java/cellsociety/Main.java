package cellsociety;

import cellsociety.config.Config;
import cellsociety.model.Grid;
import cellsociety.model.Simulation;
import cellsociety.model.celltypes.BasicCell;
import cellsociety.model.variations.GameOfLife;
import cellsociety.model.variations.Schelling;
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

  // kind of data files to look for
  public static final String DATA_FILE_EXTENSION = "*.xml";
  // default to start in the data folder to make it easy on the user to find
  public static final String DATA_FILE_FOLDER =
      System.getProperty("user.dir") + File.separator + "data";
  // internal configuration file
  public static final String INTERNAL_CONFIGURATION = "cellsociety.Version";
  // NOTE: make ONE chooser since generally accepted behavior
  // is that it remembers where user left it last
  private static final FileChooser FILE_CHOOSER = makeChooser(DATA_FILE_EXTENSION);

  // set some sensible defaults when the FileChooser is created
  private static FileChooser makeChooser(String extensionAccepted) {
    FileChooser result = new FileChooser();
    result.setTitle("Open Data File");
    // pick a reasonable place to start searching for files
    result.setInitialDirectory(new File(DATA_FILE_FOLDER));
    result.getExtensionFilters()
        .setAll(new FileChooser.ExtensionFilter("Data Files", extensionAccepted));
    return result;
  }

  /**
   * Start the program, give complete control to JavaFX.
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
    // init config, read using config and get the info organized
    // then pass the info to the view
    //hard code game of life simulation

//    Simulation<BasicCell> gameOfLife = new GameOfLife();
//    Grid<BasicCell> grid = new Grid<>(config.getWidth(), config.getHeight(), config.getGrid(),
//        gameOfLife);

//    Simulation<BasicCell> percolation = new Percolation();
//    Grid<BasicCell> grid = new Grid<>(config.getWidth(), config.getHeight(), config.getGrid(),
//        percolation);

//      Simulation<BasicCell> spreadingOfFire = new SpreadingOfFire();
//      Grid<BasicCell> grid = new Grid<>(config.getWidth(), config.getHeight(), config.getGrid(),
//        spreadingOfFire);

//      Simulation<WaTorCell> wator = new WaTor();
//      Grid<WaTorCell> grid = new Grid<>(config.getWidth(), config.getHeight(), config.getGrid(),
//          wator);

//    View mainView = new View(primaryStage, grid, config.getParameters(),
//        config.getSimulationTextInfo());
//
//    mainView.start();
    makeSimulation(primaryStage);
    showMessage(AlertType.INFORMATION, "Press N to start another simulation");
    primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
      switch (event.getCode()) {
        case N:
          Stage newStage = new Stage();
          makeSimulation(newStage);
          break;
      }
    });

  }

  private void makeSimulation(Stage primaryStage) {
    try {
      Config config = new Config();
      showMessage(AlertType.INFORMATION, String.format("Choose Simulation Configuration File"));
      File dataFile = FILE_CHOOSER.showOpenDialog(primaryStage);
      if (dataFile != null) {
        config.loadXMLFile(dataFile);
        Simulation<BasicCell> schelling = new Schelling();
        Grid<BasicCell> grid = new Grid<>(config.getWidth(), config.getHeight(), config.getGrid(),
            schelling);

        Display newDisplay = new Display(primaryStage, grid, config);
        newDisplay.start();
      }
    } catch (Exception e) {
      showMessage(AlertType.ERROR, e.getMessage());
    }
  }



  // display given message to user using the given type of Alert dialog box
  void showMessage(AlertType type, String message) {
    Alert alert = new Alert(type, message);
    alert.setTitle("Cell Society");
    alert.setHeaderText("");
    alert.showAndWait();
  }
}

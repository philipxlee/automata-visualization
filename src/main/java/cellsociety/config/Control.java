package cellsociety.config;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Control {


  // kind of data files to look for
  public static final String DATA_FILE_EXTENSION = "*.xml";
  // default to start in the data folder to make it easy on the user to find
  public static final String DATA_FILE_FOLDER =
      System.getProperty("user.dir") + File.separator + "data";

  // NOTE: make ONE chooser since generally accepted behavior
  // is that it remembers where user left it last
  private static final FileChooser FILE_CHOOSER = makeChooser(DATA_FILE_EXTENSION);


  public Control() {

  }

  // set some sensible defaults when the FileChooser is created
  private static FileChooser makeChooser(String extensionAccepted) {
    FileChooser result = new FileChooser();
    result.setTitle("Open Data File");
    result.setInitialDirectory(new File(DATA_FILE_FOLDER));
    result.getExtensionFilters()
        .setAll(new FileChooser.ExtensionFilter("Data Files", extensionAccepted));
    return result;
  }

  public void makeSimulation(Stage primaryStage) {
    Config config = new Config();
    Display.showMessage(AlertType.INFORMATION, String.format("Choose Simulation Configuration File"));
    File dataFile = FILE_CHOOSER.showOpenDialog(primaryStage);
    if (dataFile != null) {
      config.loadXmlFile(dataFile);
      Simulation<Cell> simulation = returnSimulation(config.getSimulationType());
      Grid<Cell> grid = new Grid<>(simulation, config);

      Display newDisplay = new Display(primaryStage, grid, config);
      newDisplay.start();


      primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
        if (event.getCode() == KeyCode.N) {
            Stage newStage = new Stage();
            makeSimulation(newStage);
        }
      });
    }
    Display.showMessage(AlertType.INFORMATION, "Press N to start another simulation");
  }

  private <T extends Cell> Simulation<T> returnSimulation(String simulationType) {
    Simulation<T> simulation;
    switch (simulationType) {
      case "GameOfLife" -> simulation = (Simulation<T>) new GameOfLife();
      case "Schelling" -> simulation = (Simulation<T>) new Schelling();
      case "Percolation" -> simulation = (Simulation<T>) new Percolation();
      case "SpreadingOfFire" -> simulation = (Simulation<T>) new SpreadingOfFire();
      case "FallingSand" -> simulation = (Simulation<T>) new FallingSand();
      case "WaTor" -> simulation = (Simulation<T>) new WaTor();
      case "ForagingAnts" -> simulation = (Simulation<T>) new ForagingAnts();
      default -> throw new ConfigurationException("Invalid Simulation Type");
    }

    return simulation;
  }

  // display given message to user using the given type of Alert dialog box
}

package cellsociety.config;

import cellsociety.view.Display;
import javafx.scene.control.Alert.AlertType;

public class ConfigurationException extends RuntimeException {

  public ConfigurationException(String message, Exception e) {
    super(message, e);
    Display.showMessage(AlertType.ERROR, message);
  }

  public ConfigurationException(String message) {
    super(message);
    Display.showMessage(AlertType.ERROR, message);
  }
}

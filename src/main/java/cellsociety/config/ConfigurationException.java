package cellsociety.config;

import cellsociety.view.Display;
import javafx.scene.control.Alert.AlertType;

public class ConfigurationException extends RuntimeException {

  /**
   * @param message the message to be displayed for exception
   * @param e       the cause of the exception
   */
  public ConfigurationException(String message, Exception e) {
    super(message, e);
    Display.showMessage(AlertType.ERROR, message);
  }

  /**
   * @param message the message to be displayed for exception
   */
  public ConfigurationException(String message) {
    super(message);
    Display.showMessage(AlertType.ERROR, message);
  }
}

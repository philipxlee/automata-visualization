package cellsociety.config;

public class ConfigurationException extends RuntimeException {
  public ConfigurationException (String message, Exception e) {
    super(message, e);
  }
}

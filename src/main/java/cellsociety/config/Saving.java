package cellsociety.config;

import cellsociety.model.Cell;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Saving {

  public static final String SAVED_FILE_FOLDER =
      System.getProperty("user.dir") + File.separator + "data" + File.separator + "SavedFile";
  public Saving() {

  }

  private Element bearTextChild(Document document, Element parent, String childName,
      String textContent) {
    Element child = bearChild(document, parent, childName);
    child.appendChild(document.createTextNode(textContent));
    return child;
  }

  private Element bearChild(Document document, Element parent, String childName) {
    Element child = createElement(document, childName);
    parent.appendChild(child);
    return child;
  }

  private Element createElement(Document document, String childName) {
    return document.createElement(childName);
  }

  private char stateToChar(String state) {
    char returnedCharacter;
    switch (state) {
      case "EMPTY" -> returnedCharacter = '0';
      case "ALIVE" -> returnedCharacter = '1';
      case "DEAD" -> returnedCharacter = '2';
      case "TREE" -> returnedCharacter = 'T';
      case "BURNING" -> returnedCharacter = 'B';
      case "X" -> returnedCharacter = 'X';
      case "O" -> returnedCharacter = 'O';
      case "FISH" -> returnedCharacter = 'F';
      case "SHARK" -> returnedCharacter = 'S';
      case "PERCOLATED" -> returnedCharacter = 'P';
      case "WALL" -> returnedCharacter = 'W';
      case "SAND" -> returnedCharacter = 'D';
      case "ANT" -> returnedCharacter = 'A';
      case "VISITED" -> returnedCharacter = 'V';
      default -> returnedCharacter = '0';
    }

    return returnedCharacter;
  }

  public static void write(Document doc, FileOutputStream fos) throws IOException {
    try {
      // Use Transformer to write XML document to a file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(fos);
      transformer.transform(source, result);
    } catch (TransformerException e) {
      throw new IOException("Error occurred while writing XML file", e);
    }
  }


  /**
   * saves the state of the simulation into XML file
   *
   * @param xmlName  The String name of the xml file to be created and written to
   * @param cellGrid The grid state to be stored in the saved file
   * @throws ParserConfigurationException
   * @throws TransformerException
   * @throws IOException
   */
  public void saveXmlFile(String xmlName, Cell[][] cellGrid, Config config) {

    File directoryPath = new File(SAVED_FILE_FOLDER);
    String xmlPath = xmlName + ".xml";
    String textPath = xmlName + "GRID.txt";

    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = null;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();

      // Create a new Document
      Document document = documentBuilder.newDocument();

      // Create the root element
      Element simulationElement = document.createElement("simulation");
      document.appendChild(simulationElement);

      Element typeElement = bearChild(document, simulationElement, "type");
      typeElement.setAttribute("id", config.getSimulationType());

      Element parametersElement = bearChild(document, typeElement, "parameters");

      Map<String, Double> parameters = config.getParameters();
      for (String key : config.getParameters().keySet()) {
        bearTextChild(document, parametersElement, key, Double.toString(parameters.get(key)));
      }

      bearTextChild(document, typeElement, "title", config.getSimulationTitle());

      bearTextChild(document, typeElement, "author", config.getAuthors());

      bearTextChild(document, typeElement, "description", config.getDescription());

      Element gridDimensionsElement = bearChild(document, typeElement, "gridDimensions");

      bearTextChild(document, gridDimensionsElement, "width",
          Integer.toString(config.getWidth()));

      bearTextChild(document, gridDimensionsElement, "height",
          Integer.toString(config.getHeight()));

      bearTextChild(document, typeElement, "fileName", textPath);

      File xmlFile = new File(directoryPath, xmlPath);

      // Write the content into XML file
      FileOutputStream fos = new FileOutputStream(xmlFile);
      write(document, fos);
      fos.close();
    } catch (ParserConfigurationException | IOException e) {
      System.out.println("fix the error");
    }

    File file = new File(directoryPath, textPath);
    try {
      file.createNewFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      Files.writeString(Path.of(textPath), "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      Files.writeString(Path.of(directoryPath + File.separator + textPath), "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    for (int row = 0; row < config.getHeight(); row++) {
      System.out.printf("height: %d and width: %d\n", config.getHeight(), config.getWidth());
      for (int col = 0; col < config.getWidth(); col++) {
        String writtenChar = stateToChar(cellGrid[row][col].getState()) + "";
        System.out.printf("row: %d and col: %d and char: %s\n", row, col, writtenChar);
        try {
          Files.writeString(Path.of(directoryPath + File.separator + textPath), writtenChar,
              StandardOpenOption.APPEND);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      try {
        Files.writeString(Path.of(directoryPath + File.separator + textPath), "\n",
            StandardOpenOption.APPEND);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}

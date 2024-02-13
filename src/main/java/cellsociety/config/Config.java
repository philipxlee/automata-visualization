package cellsociety.config;


import static java.util.Map.entry;

import cellsociety.model.Cell;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Config {

  public static final String DEFAULT_LANGUAGE = "English";
  public static final String DEFAULT_EDGE_POLICY = "Normal";
  public static final String DEFAULT_DIMENSION = "50";
  public static final String SAXEXCEPTION_MESSAGE = "SAXException:  invalid XML syntax, "
      + "unexpected XML structure, or errors during the parsing process";

  public static final String IOEXCEPTION_MESSAGE = "IOException: cannot read from file";

  public static final String PARSER_CONFIG_EXCEPTION_MESSAGE = "ParserConfigurationException: "
      + "incorrect parser settings or features not supported by the underlying XML parser";
  private static final Map<String, Color> DEFAULT_STATE_COLORS = Map.ofEntries(
      entry("ALIVE", Color.BLACK),
      entry("EMPTY", Color.WHITE),
      entry("PERCOLATED", Color.BLUE),
      entry("WALL", Color.BLACK),
      entry("BURNING", Color.RED),
      entry("TREE", Color.GREEN),
      entry("FISH", Color.ORANGE),
      entry("SHARK", Color.DARKGRAY),
      entry("X", Color.RED),
      entry("O", Color.BLUE),
      entry("SAND", Color.PEACHPUFF),
      entry("ANT", Color.SADDLEBROWN),
      entry("VISITED", Color.DARKBLUE)
  );
  private String simulationType;
  private String simulationTitle;
  private String authors;
  private String description;
  private String edgePolicy;
  // ideally combine them in a tuple
  private int width;
  private int height;
  private String language;
  private char[][] grid;
  private final Queue<Character> cellValues;
  private Map<String, Double> parameters;
  private Map<String, Color> stateColors;


  public Config() {
    parameters = new HashMap<>();
    stateColors = new HashMap<>();
    cellValues = new LinkedList<>();
  }



  public void loadXmlFile(File xmlFile) {

    Document doc = null;
    try {
      doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
    } catch (SAXException e) {
      new Control().showMessage(AlertType.ERROR, String.format(SAXEXCEPTION_MESSAGE));
    } catch (IOException e) {
      new Control().showMessage(AlertType.ERROR, String.format(IOEXCEPTION_MESSAGE));
    } catch (ParserConfigurationException e) {
      new Control().showMessage(AlertType.ERROR, String.format(PARSER_CONFIG_EXCEPTION_MESSAGE));
    }

    doc.getDocumentElement().normalize();

    simulationType = ((Element) tagToNode(doc, "type")).getAttribute("id");

    parameters = (Map<String, Double>) putChildren("double", doc, "parameters");

    setSimulationInfo(doc);
    setSimulationDimensions(doc);
    //adjustColors(stateColors, doc, "stateColors");

    String fileName = getTagText(doc, "fileName", "");
    System.out.println(fileName);
    grid = fileToGrid(fileName);


  }

  private void adjustColors(Map<String, Color> colors, Document doc, String item) {
    colors = (Map<String, Color>) putChildren("color", doc, item);
    if (colors.isEmpty()) {
      colors = DEFAULT_STATE_COLORS;
    }
  }

  private void setSimulationDimensions(Document doc) {
    width = returnInteger(getTagText(doc, "width", DEFAULT_DIMENSION));
    height = returnInteger(getTagText(doc, "height", DEFAULT_DIMENSION));
  }

  private void setSimulationInfo(Document doc) {
    simulationTitle = getTagText(doc, "title", "");
    authors = getTagText(doc, "authors", "");
    description = getTagText(doc, "description", "");
    edgePolicy = getTagText(doc, "edgePolicy", DEFAULT_EDGE_POLICY);
    language = getTagText(doc, "language", DEFAULT_LANGUAGE);
  }

  private int returnInteger(String intString) {
    int returnedInt;
    try {
      returnedInt = Integer.parseInt(intString);
    } catch (NumberFormatException e) {
      returnedInt = 50;
    }
    return returnedInt;
  }

  private Map<String, ?> putChildren(String valueType, Document document, String item) {
    Map<String, ?> map = new HashMap<>();
    NodeList nodeList = returnChildNodes(document, item);
    if (nodeList == null) {
      return map;
    }


    for (int i = 0; i < nodeList.getLength(); i++) {
      Node currentNode = nodeList.item(i);

      if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) currentNode;
        String tagName = element.getTagName();
        String textContent = element.getTextContent().trim();

        if (valueType.equals("double")) {
          try {
            double value = Double.parseDouble(textContent);
            ((Map<String, Double>) map).put(tagName, value);
          } catch (NumberFormatException e) {
            // Handle parsing error
            new Control().showMessage(AlertType.ERROR,
                String.format("Error parsing double value for tag: %s", tagName));
          }
        } else if (valueType.equals("color")) {
          Color color = Color.web(textContent);
          ((Map<String, Color>) map).put(tagName, color);
        } else {
          ((Map<String, String>) map).put(tagName, textContent);
        }

      } else if (currentNode.getNodeType() == Node.TEXT_NODE &&
          !currentNode.getTextContent().trim().isEmpty()) {
        System.out.println("Testing: " + currentNode.getTextContent().trim());
      }
    }

    return map;
  }


  public String getEdgePolicy() {
    return edgePolicy; // placeholder, but just return whatever edge policy it is as a string
    // it can only be "Normal" or "Vertical"
  }


  private Node tagToNode(Document document, String tag) {
    NodeList nodeList = document.getElementsByTagName(tag);
    return nodeList.item(0);
  }

  private String getTagText(Document document, String tag, String defaultReturn) {
    Element element = (Element) tagToNode(document, tag);
    if (element != null) {
      return element.getTextContent().trim();
    } else {
      return defaultReturn;
    }
  }


  private NodeList returnChildNodes(Document document, String tag) {
    Node node = tagToNode(document, tag);
    if (node != null) {
      return node.getChildNodes();
    } else {
      return null;
    }
  }


  /**
   * @return a grid of characters which represents the initial state of the cells at the start of
   * the simulation
   */
  public char[][] getGrid() {
    return grid;
  }

  /**
   * @return a map of parameters and their values from the XML file
   */
  public Map<String, Double> getParameters() {
    return parameters;
  }

  /**
   * @return an array of Strings which contain information about the simulation type, title,
   * authors, and description
   */
  public String[] getSimulationTextInfo() {
    return new String[]{simulationType, simulationTitle, authors, description};
  }

  public String getSimulationType() {
    return simulationType;
  }


  /**
   * @return an int which is the width of given in the XML file
   */
  public int getWidth() {
    return width;
  }

  /**
   * @return an int which is the height given in the XML file
   */
  public int getHeight() {
    return height;
  }

  public String getLanguage() {
    return language;
  }

  private char[][] fileToGrid(String path) {
    System.out.println("check");
    char[][] fileGrid = new char[height][width];
    String fullPath = Control.DATA_FILE_FOLDER + File.separator + path;

    try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
      for (int i = 0; i < height; i++) {
        String line = reader.readLine();
        for (int j = 0; j < width; j++) {
          try {
            fileGrid[i][j] = line.charAt(j);
            System.out.println(fileGrid[i][j]);
          } catch (IndexOutOfBoundsException | NullPointerException e) {
            fileGrid[i][j] = '0';
          }
          cellValues.offer(fileGrid[i][j]);
        }
      }
    } catch (IOException e) {
      // Handle exceptions, such as file not found or unable to read
      e.printStackTrace();
    }

    return fileGrid;
  }


  // If the cellValues is empty, only return the value of empty cell
  public char nextCellValue() {
    Character c = cellValues.poll();
    System.out.println(c);
    if (c != null) {
      return c;
    } else {
      return '0';
    }
  }

  public Iterator<Entry<String, Color>> getStateColorsIterator() {
    return Collections.unmodifiableMap(stateColors).entrySet().iterator();
  }

  public String getSimulationTitle() {
    return simulationTitle;
  }

  public String getAuthors() {
    return authors;
  }

  public String getDescription() {
    return description;
  }

//  public static void main(String[] args) throws Exception {
//    Config newConfig = new Config();
//    newConfig.loadXmlFile(new File("C:\\Users\\Ashitaka\\CS308\\cellsociety_team03\\data\\SpreadingOfFire\\SpreadingOfFire1.xml"));
//    System.out.println(newConfig.getStateColors().get("ALIVE").toString());
//  }

}

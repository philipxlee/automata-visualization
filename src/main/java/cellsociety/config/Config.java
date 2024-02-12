package cellsociety.config;


import static java.util.Map.entry;

import cellsociety.Main;
import cellsociety.model.Cell;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import javafx.scene.paint.Color;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Config {

  public static final String DEFAULT_LANGUAGE = "English";
  public static final String DEFAULT_EDGE_POLICY = "Normal";
  public static final String DEFAULT_DIMENSION = "50";
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
      entry("ANT", Color.INDIANRED),
      entry("HOME", Color.BLUE),
      entry("FOOD", Color.ORANGE),
      entry("HIGHPHEROMONE", Color.DARKGREEN),
      entry("LOWPHEROMONE", Color.LIGHTGREEN),
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
  private Queue<Character> cellValues;
  private Map<String, Double> parameters;
  private Map<String, Color> stateColors;
  public static final String SAVED_FILE_FOLDER =
      System.getProperty("user.dir") + File.separator + "data" + File.separator + "SavedFile";


  public Config() {
    parameters = new HashMap<>();
    stateColors = new HashMap<>();
    cellValues = new LinkedList<>();
  }

  public void loadXmlFile(File xmlFile) throws Exception {

    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);

    doc.getDocumentElement().normalize();

    simulationType = ((Element) tagToNode(doc, "type")).getAttribute("id");

    putChildren(parameters, doc, "parameters");

    simulationTitle = getTagText(doc, "title", "");
    authors = getTagText(doc, "authors", "");
    description = getTagText(doc, "description", "");
    edgePolicy = getTagText(doc, "edgePolicy", DEFAULT_EDGE_POLICY);
    language = getTagText(doc, "language", DEFAULT_LANGUAGE);

    width = Integer.parseInt(getTagText(doc, "width", DEFAULT_DIMENSION));
    height = Integer.parseInt(getTagText(doc, "height", DEFAULT_DIMENSION));

    putColors(stateColors, doc, "stateColors");
    if(stateColors.isEmpty()){
      stateColors = DEFAULT_STATE_COLORS;
    }

    String fileName = getTagText(doc, "fileName", "");
    grid = fileToGrid(fileName);


  }

  private void putChildren(Map<String, Double> map, Document document, String item) {
    NodeList nodeList = returnChildNodes(document, item);
    if (nodeList == null) {
    }

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node currentNode = nodeList.item(i);

      if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
        // Process the element node
        Element element = (Element) currentNode;
        map.put(element.getTagName(), Double.parseDouble(element.getTextContent().trim()));

      } else if (currentNode.getNodeType() == Node.TEXT_NODE &&
          !currentNode.getTextContent().trim().isEmpty()) {
        System.out.println("Testing: " + currentNode.getTextContent().trim());
      }
    }
  }

  private void putColors(Map<String, Color> map, Document document, String item) {
    NodeList nodeList = returnChildNodes(document, item);
    if (nodeList == null) {
      return;
    }

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node currentNode = nodeList.item(i);

      if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
        // Process the element node
        Element element = (Element) currentNode;
        map.put(element.getTagName(), Color.web(element.getTextContent().trim()));
//        map.put(element.getTagName(), Color.decode(element.getTextContent().trim()));

      } else if (currentNode.getNodeType() == Node.TEXT_NODE &&
          !currentNode.getTextContent().trim().isEmpty()) {
        System.out.println("Testing: " + currentNode.getTextContent().trim());
      }
    }
  }


  /**
   * saves the state of the simulation into XML file
   *
   * @param xmlName The String name of the xml file to be created and written to
   * @param cellGrid    The grid state to be stored in the saved file
   * @throws ParserConfigurationException
   * @throws TransformerException
   * @throws IOException
   */
  public void saveXmlFile(String xmlName, Cell[][] cellGrid) {

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
      typeElement.setAttribute("id", simulationType);

      Element parametersElement = bearChild(document, typeElement, "parameters");

      for (String key : parameters.keySet()) {
        bearTextChild(document, parametersElement, key, Double.toString(parameters.get(key)));
      }

      bearTextChild(document, typeElement, "title", simulationTitle);

      bearTextChild(document, typeElement, "author", authors);

      bearTextChild(document, typeElement, "description", description);

      Element gridDimensionsElement = bearChild(document, typeElement, "gridDimensions");

      bearTextChild(document, gridDimensionsElement, "width",
          Integer.toString(width));

      bearTextChild(document, gridDimensionsElement, "height",
          Integer.toString(height));


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
    for (int row = 0; row < height; row++) {
      System.out.printf("height: %d and width: %d\n", height, width);
      for (int col = 0; col < width; col++) {
        String writtenChar = stateToChar(cellGrid[row][col].getState()) + "";
        System.out.printf("row: %d and col: %d and char: %s\n", row, col, writtenChar);
        try {
          Files.writeString(Path.of(directoryPath + File.separator + textPath), writtenChar, StandardOpenOption.APPEND);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      try {
        Files.writeString(Path.of(directoryPath + File.separator + textPath), "\n", StandardOpenOption.APPEND);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
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

  private Element createElement(Document document, String childName) {
    return document.createElement(childName);
  }

  private Element bearTextChild(Document document, Element parent, String childName,
      String textContent) {
    Element child = bearChild(document, parent, childName);
    child.appendChild(document.createTextNode(textContent));
    return child;
  }


  private char[][] fileToGrid(String path) {
    char[][] fileGrid = new char[height][width];
    String fullPath = Control.DATA_FILE_FOLDER + File.separator + path;

    try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
      for (int i = 0; i < height; i++) {
        String line = reader.readLine();
        for (int j = 0; j < width; j++) {
          try {
            fileGrid[i][j] = line.charAt(j);
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


  private Element bearChild(Document document, Element parent, String childName) {
    Element child = createElement(document, childName);
    parent.appendChild(child);
    return child;
  }

  // If the cellValues is empty, only return the value of empty cell
  public char nextCellValue() {
    Character c = cellValues.poll();
    if (c != null) {
      return c;
    } else {
      return '0';
    }
  }

  public Iterator<Entry<String, Color>> getStateColorsIterator() {
    return Collections.unmodifiableMap(stateColors).entrySet().iterator();
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
      default -> returnedCharacter ='0';
    }

    return returnedCharacter;
  }

//  public static void main(String[] args) throws Exception {
//    Config newConfig = new Config();
//    newConfig.loadXmlFile(new File("C:\\Users\\Ashitaka\\CS308\\cellsociety_team03\\data\\SpreadingOfFire\\SpreadingOfFire1.xml"));
//    System.out.println(newConfig.getStateColors().get("ALIVE").toString());
//  }

}

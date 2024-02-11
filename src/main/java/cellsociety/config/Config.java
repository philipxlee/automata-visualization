package cellsociety.config;


import cellsociety.Main;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
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
  private String simulationType;
  private String simulationTitle;
  private String authors;
  private String description;

  // ideally combine them in a tuple
  private int width;
  private int height;
  private String language;
  private char[][] grid;
  private Queue<Character> cellValues;
  private Map<String, Double> parameters;
  private Map<String, Color> stateColors;


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

    simulationTitle = getTagText(doc, "title");
    authors = getTagText(doc, "authors");
    description = getTagText(doc, "description");
    language = getTagText(doc, "language");
    if (language.isEmpty()) {
      language = DEFAULT_LANGUAGE;
    }

    width = Integer.parseInt(getTagText(doc, "width"));
    height = Integer.parseInt(getTagText(doc, "height"));

    putColors(stateColors, doc, "stateColors");

    String fileName = getTagText(doc, "fileName");
    grid = fileToGrid(fileName);


  }

  private void putChildren(Map<String, Double> map, Document document, String item) {
    NodeList nodeList = returnChildNodes(document, item);
    if (nodeList == null) {
      return;
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
        map.put(element.getTagName(), Color.decode(element.getTextContent().trim()));

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
   * @param grid    The grid state to be stored in the saved file
   * @throws ParserConfigurationException
   * @throws TransformerException
   * @throws IOException
   */
  public void saveXmlFile(String xmlName, char[][] grid)
      throws ParserConfigurationException, TransformerException, IOException {

    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

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

    String textPath = xmlName + "GRID";
    bearTextChild(document, typeElement, "fileName", textPath);

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();

    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

    String path = xmlName + ".xml";
    DOMSource source = new DOMSource(document);
    StreamResult result = new StreamResult(new java.io.File(path)); // Specify the output file

    transformer.transform(source, result);

    File file = new File(textPath);
    file.createNewFile();
    Files.writeString(Path.of(textPath), "");

    for (int row = 0; row < height; row++) {
      String rowString = String.valueOf(grid[row]);
      Files.writeString(Path.of(textPath), rowString, StandardOpenOption.APPEND);
      Files.writeString(Path.of(textPath), "\n", StandardOpenOption.APPEND);
    }
  }

  public void editParameter() {

  }

  public String getEdgePolicy() {
    System.out.println("Reached config");
    return "VerticalSplit";
  }


  private Node tagToNode(Document document, String tag) {
    NodeList nodeList = document.getElementsByTagName(tag);
    return nodeList.item(0);
  }

  private String getTagText(Document document, String tag) {
    Element element = (Element) tagToNode(document, tag);
    if (element != null) {
      return element.getTextContent().trim();
    } else {
      return "";
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
    String fullPath = Main.DATA_FILE_FOLDER + File.separator + path;

    try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
      for (int i = 0; i < height; i++) {
        String line = reader.readLine();
        for (int j = 0; j < width; j++) {
          fileGrid[i][j] = line.charAt(j);
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

  public Map<String, Color> getStateColors() {
    return stateColors;
  }

//  public static void main(String[] args) throws Exception {
//    Config newConfig = new Config();
//    newConfig.loadXmlFile(new File("C:\\Users\\Ashitaka\\CS308\\cellsociety_team03\\data\\SpreadingOfFire\\SpreadingOfFire.xml"));
//    System.out.println(newConfig.getStateColors().get("ALIVE").toString());
//  }

}

package cellsociety.config;


import cellsociety.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
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

  private String simulationType;
  private String simulationTitle;
  private String authors;
  private String description;

  // ideally combine them in a tuple
  private int width;
  private int height;
  private char[][] grid;
  private Map<String, Double> parameters;

  public Config() {
    grid = new char[10][10];
    parameters = new HashMap<>();
  }

  /**
   * @param path The String path of the xml file to be loaded and read
   * @throws Exception
   */
  // make sure to take care of the Exception
  public void loadXMLFilePhilipTest(String path) throws Exception {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(path);

    doc.getDocumentElement().normalize();

    simulationType = ((Element) tagToNode(doc, "type")).getAttribute("id");

    NodeList parameterList = getChildNodes(doc, "parameters");
    for (int i = 0; i < parameterList.getLength(); i++) {
      Node currentNode = parameterList.item(i);

      if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
        // Process the element node
        Element element = (Element) currentNode;
        parameters.put(element.getTagName(), Double.parseDouble(element.getTextContent().trim()));

      } else if (currentNode.getNodeType() == Node.TEXT_NODE &&
          !currentNode.getTextContent().trim().isEmpty()) {
        System.out.println("Testing: " + currentNode.getTextContent().trim());
      }
    }

    simulationTitle = getTagText(doc, "title");
    authors = getTagText(doc, "authors");
    description = getTagText(doc, "description");
    width = Integer.parseInt(getTagText(doc, "width"));
    height = Integer.parseInt(getTagText(doc, "height"));

    String fileName = getTagText(doc, "fileName");
    grid = fileToGrid(fileName);


  }


  public void loadXMLFile(File xmlFile) throws Exception {

    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);

    doc.getDocumentElement().normalize();

    simulationType = ((Element) tagToNode(doc, "type")).getAttribute("id");

    NodeList parameterList = getChildNodes(doc, "parameters");
    for (int i = 0; i < parameterList.getLength(); i++) {
      Node currentNode = parameterList.item(i);

      if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
        // Process the element node
        Element element = (Element) currentNode;
        parameters.put(element.getTagName(), Double.parseDouble(element.getTextContent().trim()));

      } else if (currentNode.getNodeType() == Node.TEXT_NODE &&
          !currentNode.getTextContent().trim().isEmpty()) {
        System.out.println("Testing: " + currentNode.getTextContent().trim());
      }
    }

    simulationTitle = getTagText(doc, "title");
    authors = getTagText(doc, "authors");
    description = getTagText(doc, "description");
    width = Integer.parseInt(getTagText(doc, "width"));
    height = Integer.parseInt(getTagText(doc, "height"));

    String fileName = getTagText(doc, "fileName");
    grid = fileToGrid(fileName);


  }


  /**
   * @param path The String path of the xml file to be created and written to
   * @param simulationTextInfo The array containing simulation type, title, authors, description
   * @param parameters The map which contains key-value pairs for each parameter's value
   * @param grid The grid state to be stored in the saved file
   * @param width The width of the grid
   * @param height The height of the grid
   * @param textPath The String path of the txt file where the state of the grid will be stored
   * @throws ParserConfigurationException
   * @throws TransformerException
   * @throws IOException
   */
  public void saveXMLFile(String path, String[] simulationTextInfo, Map<String, Double> parameters,
      char[][] grid, int width, int height, String textPath)
      throws ParserConfigurationException, TransformerException, IOException {

    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

    // Create a new Document
    Document document = documentBuilder.newDocument();

    // Create the root element
    Element simulationElement = document.createElement("simulation");
    document.appendChild(simulationElement);

    Element typeElement = bearChild(document, simulationElement, "type");
    typeElement.setAttribute("id", simulationTextInfo[0]);

    Element parametersElement = bearChild(document, typeElement, "parameters");

    for (String key : parameters.keySet()) {
      bearTextChild(document, parametersElement, key, Double.toString(parameters.get(key)));
    }

    bearTextChild(document, typeElement, "title", simulationTextInfo[1]);

    bearTextChild(document, typeElement, "author", simulationTextInfo[2]);

    bearTextChild(document, typeElement, "description",
        simulationTextInfo[3]);

    Element gridDimensionsElement = bearChild(document, typeElement, "gridDimensions");

    bearTextChild(document, gridDimensionsElement, "width",
        Integer.toString(width));

    bearTextChild(document, gridDimensionsElement, "height",
        Integer.toString(height));

    bearTextChild(document, typeElement, "fileName", textPath);

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();

    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

    DOMSource source = new DOMSource(document);
    StreamResult result = new StreamResult(new java.io.File(path)); // Specify the output file

    transformer.transform(source, result);

    File file = new File(textPath);
    file.createNewFile();
    Files.writeString(Path.of(textPath), "");

    for (int row = 0; row < height; row++) {
      String rowString = String.valueOf(grid[row]);
      System.out.println(rowString);
      Files.writeString(Path.of(textPath), rowString, StandardOpenOption.APPEND);
      Files.writeString(Path.of(textPath), "\n", StandardOpenOption.APPEND);
    }
  }

  public void editParameter() {

  }


  private Node tagToNode(Document document, String tag) {
    NodeList nodeList = document.getElementsByTagName(tag);
    return nodeList.item(0);
  }

  private String getTagText(Document document, String tag) {
    Element element = (Element) tagToNode(document, tag);
    return element.getTextContent().trim();
  }


  private NodeList getChildNodes(Document document, String tag) {
    Node node = tagToNode(document, tag);
    return node.getChildNodes();
  }


  /**
   * @return a grid of characters which represents the initial state
   * of the cells at the start of the simulation
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
   * @return an array of Strings which contain information about the
   * simulation type, title, authors, and description
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
      int character;

      for (int i = 0; i < height; i++) {
        String line = reader.readLine();
        for (int j = 0; j < width; j++) {
          fileGrid[i][j] = line.charAt(j);
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

//  public static void main(String[] args) throws Exception {
//    Config newConfig = new Config();
//    newConfig.loadXMLFile(new File("C:\\Users\\Ashitaka\\CS308\\cellsociety_team03\\data\\Schelling\\Schelling1.xml"));
//
//  }

}

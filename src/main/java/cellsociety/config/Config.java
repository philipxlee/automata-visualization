package cellsociety.config;


import static java.util.Map.entry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import javafx.scene.paint.Color;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Config {

  public static final int DEFAULT_DIMENSION = 0;
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
  private final Queue<Character> cellValues;
  private String simulationType;
  private String simulationTitle;
  private String authors;
  private String description;
  private String edgePolicy;
  // ideally combine them in a tuple
  private int width;
  private int height;
  private String language;
  private Map<String, Double> parameters;
  private final Map<String, Color> stateColors;


  public Config() {
    parameters = new HashMap<>();
    stateColors = new HashMap<>(DEFAULT_STATE_COLORS);
    cellValues = new LinkedList<>();
  }


  public void loadXmlFile(File xmlFile) {

    Document doc = null;
    try {
      doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
    } catch (SAXException e) {
      throw new ConfigurationException("SAXException: Invalid syntax or XML structure", e);
    } catch (IOException e) {
      throw new ConfigurationException("IOException: Cannot read from file", e);
    } catch (ParserConfigurationException e) {
      throw new ConfigurationException("ParserConfigurationException: incorrect parser settings or "
          + "features not supported by the underlying XML parser", e);
    }

    doc.getDocumentElement().normalize();

    if ((simulationType = ((Element) tagToNode(doc, "type")).getAttribute("id")) == null) {
      throw new ConfigurationException("Missing Simulation Type");
    }

    parameters = returnDoubleChildren(returnChildren(doc, "parameters"));

    setSimulationInfo(doc);
    setSimulationDimensions(doc);
    returnColorChildren(returnChildren(doc, "stateColors"), stateColors);

    String fileName = getTagText(doc, "fileName");
    fileToQueue(fileName);


  }


  private void setSimulationDimensions(Document doc) {
    width = getTagInt(doc, "width");
    height = getTagInt(doc, "height");
    if (width < 0 || height < 0) {
      throw new ConfigurationException("Invalid width or height");
    }
  }

  private int getTagInt(Document doc, String tag) {
    Element element = (Element) tagToNode(doc, tag);
    if (element != null) {
      return Integer.parseInt(element.getTextContent().trim());
    } else {
      return DEFAULT_DIMENSION;
    }
  }

  private void setSimulationInfo(Document doc) {
    simulationTitle = getTagText(doc, "title");
    authors = getTagText(doc, "authors");
    description = getTagText(doc, "description");
    edgePolicy = getTagText(doc, "edgePolicy");
//    if (!edgePolicy.equals("Normal") && !edgePolicy.equals("Vertical")) {
//      throw new ConfigurationException("Bad Edge Policy");
//    }
    language = getTagText(doc, "language");
  }


  private Map<String, String> returnChildren(Document document, String item) {
    Map<String, String> map = new HashMap<>();
    NodeList nodeList = returnChildNodes(document, item);
    if (nodeList == null) {
      return map;
    }

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node currentNode = nodeList.item(i);

      if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) currentNode;
        String tagName = element.getTagName();

        map.put(tagName, element.getTextContent().trim());

      }
    }

    return map;
  }

  private Map<String, Double> returnDoubleChildren(Map<String, String> oldMap) {
    Map<String, Double> newMap = new HashMap<>();
    for (String key : oldMap.keySet()) {
      newMap.put(key, Double.parseDouble(oldMap.get(key)));
    }
    return newMap;
  }

  private void returnColorChildren(Map<String, String> oldMap, Map<String, Color> colors) {
    for (String key : oldMap.keySet()) {
      colors.put(key, Color.web(oldMap.get(key)));
    }
  }


  public String getEdgePolicy() {
    return edgePolicy;
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
      throw new ConfigurationException(String.format("%s is missing", tag));
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

  private void fileToQueue(String path) {
    String fullPath = Control.DATA_FILE_FOLDER + File.separator + path;
    int rows = 0;

    try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        rows += 1;
        if (width == DEFAULT_DIMENSION) {
          width = line.length();
        } else if (width < line.length()) {
          throw new ConfigurationException("Outside grid columns' bounds");
        }
        for (int j = 0; j < line.length(); j++) {
          try {
            cellValues.offer(line.charAt(j));
          } catch (IndexOutOfBoundsException | NullPointerException e) {
            cellValues.offer('0');
          }
        }
      }
    } catch (IOException e) {
      throw new ConfigurationException("IOException: Make sure that the addressed file exists", e);
    }

    if (height == DEFAULT_DIMENSION) {
      height = rows;
    } else if (height < rows) {
      throw new ConfigurationException("Outside grid rows' bounds");
    }

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

  public String getSimulationTitle() {
    return simulationTitle;
  }

  public String getAuthors() {
    return authors;
  }

  public String getDescription() {
    return description;
  }


}

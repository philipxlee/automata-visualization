package cellsociety.Config;


import cellsociety.Main;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    private String fileName;
    private char[][] grid;
    private HashMap<String, Double> parameters;

    public Config() {
        grid = new char[10][10];
        parameters = new HashMap<>();
    }

    // make sure to take care of the Exception
    public void loadXMLFile(String path) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(path);

        doc.getDocumentElement().normalize();

        simulationType = getAttribute(doc, "type", "id");

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
        fileName = getTagText(doc, "fileName");
        grid = fileToGrid(fileName);


    }

    public void saveXMLFile(String path) {

    }

    public void editParameter() {

    }

    public void start() {

    }

    private Node tagToNode (Document document, String tag) {
        NodeList nodeList = document.getElementsByTagName(tag);
        return nodeList.item(0);
    }

    private String getTagText (Document document, String tag) {
        Element element = (Element) tagToNode(document, tag);
        return element.getTextContent().trim();
    }

    private String getAttribute (Document document, String tag, String attribute) {
        Element element = (Element) tagToNode(document, tag);
        return element.getAttribute(attribute);
    }

    private NodeList getChildNodes (Document document, String tag) {
        Node node = tagToNode(document, tag);
        return node.getChildNodes();
    }

    private char[][] fileToGrid(String path) {
        char[][] fileGrid = new char[10][10];
        String fullPath = Main.DATA_FILE_FOLDER + File.separator + path;

        try (FileReader reader = new FileReader(fullPath)) {
            int character;

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    character = reader.read();
                    if (character != -1) {
                        fileGrid[i][j] = (char) character;
                    }
                }
                reader.read();
                reader.read();
            }
        } catch (IOException e) {
            // Handle exceptions, such as file not found or unable to read
            e.printStackTrace();
        }

        return fileGrid;
    }

    public char[][] getGrid() {
        return grid;
    }

    public HashMap<String, Double> getParameters() {
        return parameters;
    }

    public String[] getSimulationTextInfo() {
        return new String[]{simulationType, simulationTitle, authors, description};
    }

//    public Double[] getDimensions() {
//        return new Double[]{width, height};
//    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

//    public static void main(String args[]) throws Exception {
//        Config myConfig = new Config();
//        myConfig.loadXMLFile("C:\\Users\\Ashitaka\\CS308\\cellsociety_team03\\test.xml");
//
//    }

}

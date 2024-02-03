package cellsociety;


import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.*;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Config {
    String simulationType;
    String simulationTitle;
    String author;
    String description;

    // ideally combine them in a tuple
    Double width;
    Double height;
    String filePath;
    HashMap<String, Integer> parameter;

    Config() {

    }

    public void loadXMLFile(String path) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(path));









    }

    public void saveXMLFile(String path) {

    }

    public void editParameter() {

    }

    public static void main(String args[]) throws Exception {
        Config myConfig = new Config();
        myConfig.loadXMLFile("C:\\Users\\Ashitaka\\CS308\\cellsociety_team03\\test.xml");

    }
}

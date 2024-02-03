package cellsociety.Config;


import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import org.w3c.dom.Document;

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

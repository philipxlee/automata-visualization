package cellsociety;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Config {
    String gameType;
    String gameTitle;

    Config() {

    }

    public void loadXMLFile(String path)  {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            try {
                Document doc = builder.parse(new File(path));
                Element sim = doc.getDocumentElement();
                NodeList mod = sim.getElementsByTagName("model");
                Element typ = (Element) mod.item(0);
                System.out.println(typ.getTagName());
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }








    }

    public void saveXMLFile(String path) {

    }

    public void editParameter() {

    }

    public static void main(String args[]) {
        Config myConfig = new Config();
        myConfig.loadXMLFile("C:\\Users\\Ashitaka\\CS308\\cellsociety_team03\\test.xml");

    }
}

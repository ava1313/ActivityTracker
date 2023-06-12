import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;

public class ReadGPXFile {
    public ArrayList<Waypoint> parseFile(String filePath) {
        ArrayList<Waypoint> waypoints = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));

            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("trkpt");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;

                    String lat = eElement.getAttribute("lat");
                    String lon = eElement.getAttribute("lon");
                    String ele = eElement.getElementsByTagName("ele").item(0).getTextContent();
                    String time = eElement.getElementsByTagName("time").item(0).getTextContent();

                    waypoints.add(new Waypoint(lat, lon, ele, time));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return waypoints;
    }
}

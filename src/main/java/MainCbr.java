import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class MainCbr {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Calendar lastTime = new GregorianCalendar();
        lastTime.add(Calendar.DAY_OF_YEAR, -90);
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/YYYY");
        try {
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();

            Map<Double, String> listValue = new HashMap<>();

            for (int j = 0; j <= 90; j++) {
                Document doc = dBuilder.parse("http://www.cbr.ru/scripts/XML_daily_eng.asp?date_req=" + formater.format(lastTime.getTime()));
                doc.getDocumentElement().normalize();
                XPath xPath =  XPathFactory.newInstance().newXPath();

                String expression = "/ValCurs/Valute";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                        doc, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node nNode = nodeList.item(i);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        listValue.put(Double.parseDouble(eElement.getElementsByTagName("Value").item(0).getTextContent().replaceAll(",","."))  ,
                                eElement.getElementsByTagName("Name").item(0).getTextContent() + " " +
                                        formater.format(lastTime.getTime()));
                    }
                }
                lastTime.add(Calendar.DAY_OF_YEAR, 1);
            }
            double maxKeyInMap=(Collections.max(listValue.keySet()));
            for (Map.Entry<Double, String> entry : listValue.entrySet()) {
                if (entry.getKey()==maxKeyInMap) {
                     System.out.println("Max: " +  entry.getKey() + " " + "\n" + entry.getValue());
                }
            }
            System.out.println("====================================");

            double minKeyInMap=(Collections.min(listValue.keySet()));
            for (Map.Entry<Double, String> entry : listValue.entrySet()) {
                if (entry.getKey()==minKeyInMap) {
                    System.out.println("Min: " +  entry.getKey() + " " + "\n" + entry.getValue());
                }
            }
            System.out.println("====================================");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
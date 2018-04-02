package utility;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLRead {

	static PropertyUtility readFromProperty = new PropertyUtility();

	/*
	 * Reading the data from XML file 
	 */
	
	public static String ReadFromXMLInput(String reqInput, String reqTag) {
		String returnValue = "No data returned: invalid input/tagname";
		try {
			File inputFile = new File(readFromProperty.getTestdata(
					"config.properties").get("InputFileLocation"));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("Input");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (reqInput.equals(eElement.getAttribute("value"))) {
						returnValue = null;
						returnValue = eElement.getElementsByTagName(reqTag)
								.item(0).getTextContent();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	/*
	 * Reading the data from XML file specific to language
	 */
	public static String ReadFromXMLInput(String lan,String reqInput, String reqTag) {
		String returnValue = "No data returned: invalid input/tagname";
		try {
			File inputFile = new File(readFromProperty.getTestdata(
					"config.properties").get(lan+"InputFileLocation"));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("Input");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (reqInput.equals(eElement.getAttribute("value"))) {
						returnValue = null;
						returnValue = eElement.getElementsByTagName(reqTag)
								.item(0).getTextContent();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

}

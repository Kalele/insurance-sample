package hepstar.sample.application;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Application {

    private static Map<String, String> readFileIntoMap(String filePath) {
        Map<String, String> dataMap = new HashMap<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split("="); 
                if (parts.length == 2) {
                    dataMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            Logger.logError("Error reading file: " + filePath);
            e.printStackTrace();
        }

        return dataMap;
    }

    private static Insured getInsured() {
        Insured insured = new Insured();

        Map<String, String> insuredData = readFileIntoMap(System.getProperty("user.dir") + "/src/hepstar/sample/application/insured.txt");

        insured.setFirstname(insuredData.getOrDefault("firstname", ""));
        insured.setSurname(insuredData.getOrDefault("surname", ""));
        insured.setId(insuredData.getOrDefault("id", ""));
        insured.setDob(insuredData.getOrDefault("dob", ""));
        insured.setResidency(insuredData.getOrDefault("residency", ""));

        return insured;
    }

    private static TravelInformation getTravelInformation() {
        TravelInformation travelInformation = new TravelInformation();

        Map<String, String> travelData = readFileIntoMap(System.getProperty("user.dir") + "/src/hepstar/sample/application/travelinformation.txt");

        travelInformation.setStartdate(travelData.getOrDefault("startdate", ""));
        travelInformation.setEnddate(travelData.getOrDefault("enddate", ""));
        travelInformation.setDeparturecountry(travelData.getOrDefault("departurecountry", ""));
        travelInformation.setCovercountry(travelData.getOrDefault("covercountry", ""));

        return travelInformation;
    }

    private static String getRequestMessage(Insured insured, TravelInformation travelInformation) {
        String requestMessage = "";

        try {
            File inputFile = new File(System.getProperty("user.dir") + "/src/hepstar/sample/application/request.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            doc.getDocumentElement().normalize();

            replaceXmlTagValue(doc, "Firstname", insured.getFirstname());
            replaceXmlTagValue(doc, "Surname", insured.getSurname());
            replaceXmlAttributeValue(doc, "Insured","ID", insured.getId());
            replaceXmlTagValue(doc, "DOB", insured.getDob());
            replaceXmlTagValue(doc, "Residency", insured.getResidency());
            replaceXmlTagValue(doc, "StartDate", travelInformation.getStartdate());
            replaceXmlTagValue(doc, "EndDate", travelInformation.getEnddate());
            replaceXmlTagValue(doc, "DepartureCountry", travelInformation.getDeparturecountry());
            replaceXmlTagValue(doc, "CoverCountry", travelInformation.getCovercountry());
            replaceXmlTagValue(doc, "Session", UUID.randomUUID().toString());

            requestMessage = convertDocumentToString(doc);

        } catch (Exception e) {
            Logger.logError("Error constructing request message");
            e.printStackTrace();
        }

        return requestMessage;
    }

    private static void replaceXmlTagValue(Document doc, String tagName, String value) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            nodeList.item(0).setTextContent(value);
        }
    }


    private static void replaceXmlAttributeValue(Document doc, String tagName, String attributeName, String value) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
    
        Node node = nodeList.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE)        
          if (nodeList.getLength() > 0) {
            Element element = (Element) node;
            if (element.hasAttribute(attributeName)) 
              element.setAttribute(attributeName, value);           
          }
    }

    private static String convertDocumentToString(Document doc) {
      try {
          TransformerFactory transformerFactory = TransformerFactory.newInstance();
          Transformer transformer = transformerFactory.newTransformer();
          transformer.setOutputProperty(OutputKeys.INDENT, "yes");

          StringWriter writer = new StringWriter();
          transformer.transform(new DOMSource(doc), new StreamResult(writer));
        
          return writer.getBuffer().toString();
      } catch (TransformerException e) {
          Logger.logError("Error converting document to string");
          e.printStackTrace();
          return "";
      }   
    }

    private static String sendRequestMessage(String requestMessage) {
        String responseMessage = "";

        try {
            URL url = new URL("https://uat.gateway.insure/products/priced");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/xml");

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestMessage.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                responseMessage = response.toString();
            }

        } catch (Exception e) {
            Logger.logError("Error sending request message");
            e.printStackTrace();
        }

        return responseMessage;
    }

    public static void main(String[] args) {
        Logger.logInfo("Application started.");

        Insured insured = getInsured();
        TravelInformation travelInformation = getTravelInformation();
        String requestMessage = getRequestMessage(insured, travelInformation);

        Logger.logInfo("Request Message: \n" + requestMessage);

        String responseMessage = sendRequestMessage(requestMessage);

        Logger.logInfo("Response Message: \n" + responseMessage);
    }
}
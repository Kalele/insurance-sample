package hepstar.sample.application;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
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

            requestMessage = doc.getDocumentElement().getTextContent();

            requestMessage = requestMessage.replace("{FIRSTNAME}", insured.getFirstname());
            requestMessage = requestMessage.replace("{SURNAME}", insured.getSurname());
            requestMessage = requestMessage.replace("{ID}", insured.getId());
            requestMessage = requestMessage.replace("{DOB}", insured.getDob());
            requestMessage = requestMessage.replace("{RESIDENCY}", insured.getResidency());
            requestMessage = requestMessage.replace("{STARTDATE}", travelInformation.getStartdate());
            requestMessage = requestMessage.replace("{ENDDATE}", travelInformation.getEnddate());
            requestMessage = requestMessage.replace("{DEPARTURECOUNTRY}", travelInformation.getDeparturecountry());
            requestMessage = requestMessage.replace("{COVERCOUNTRY}", travelInformation.getCovercountry());
            requestMessage = requestMessage.replace("{SESSION}", UUID.randomUUID().toString());

        } catch (Exception e) {
            Logger.logError("Error constructing request message");
            e.printStackTrace();
        }

        return requestMessage;
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
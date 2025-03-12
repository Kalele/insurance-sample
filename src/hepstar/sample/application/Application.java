package hepstar.sample.application;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Application {

    private static Insured getInsured() {
        Insured insured = new Insured();

        try {
            List<String> lines = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/hepstar/sample/application/insured.txt"));
            insured.setFirstname(lines.get(0));
            insured.setSurname(lines.get(1));
            insured.setId(lines.get(2));
            insured.setDob(lines.get(3));
            insured.setResidency(lines.get(4));
            Logger.logInfo("Insured information loaded successfully.");
        } catch (IOException e) {
            Logger.logError("Failed to read insured file: " + e.getMessage());
        }

        return insured;
    }

    private static TravelInformation getTravelInformation() {
        TravelInformation travelInformation = new TravelInformation();

        try {
            List<String> lines = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/hepstar/sample/application/travelinformation.txt"));
            travelInformation.setStartdate(lines.get(0));
            travelInformation.setEnddate(lines.get(1));
            travelInformation.setDeparturecountry(lines.get(2));
            travelInformation.setCovercountry(lines.get(3));
            Logger.logInfo("Travel information loaded successfully.");
        } catch (IOException e) {
            Logger.logError("Failed to read travel information file: " + e.getMessage());
        }

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

            Logger.logInfo("Request message created successfully.");
        } catch (Exception e) {
            Logger.logError("Error while creating request message: " + e.getMessage());
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

            Logger.logInfo("Request sent successfully. Received response.");
        } catch (Exception e) {
            Logger.logError("Error while sending request: " + e.getMessage());
        }

        return responseMessage;
    }

    public static void main(String[] args) {
        Logger.logInfo("Application started.");

        Insured insured = getInsured();
        TravelInformation travelInformation = getTravelInformation();
        String requestMessage = getRequestMessage(insured, travelInformation);

        Logger.logDebug("Request Message: " + requestMessage);
        
        String responseMessage = sendRequestMessage(requestMessage);

        Logger.logInfo("Response: " + responseMessage);

        Logger.logInfo("Application finished.");
    }
}
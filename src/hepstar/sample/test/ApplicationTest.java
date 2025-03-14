package hepstar.sample.test;

import hepstar.sample.application.Application;
import hepstar.sample.application.Insured;
import hepstar.sample.application.TravelInformation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;

public class ApplicationTest {

    @Test
    public void testGetRequestMessage() throws Exception {
        DocumentBuilderFactory mockDbFactory = mock(DocumentBuilderFactory.class);
        DocumentBuilder mockDocBuilder = mock(DocumentBuilder.class);
        Document mockDocument = mock(Document.class);
        TransformerFactory mockTransformerFactory = mock(TransformerFactory.class);
        Transformer mockTransformer = mock(Transformer.class);

        when(mockDbFactory.newDocumentBuilder()).thenReturn(mockDocBuilder);
        when(mockDocBuilder.parse(any(File.class))).thenReturn(mockDocument);
        when(mockTransformerFactory.newTransformer()).thenReturn(mockTransformer);

        mockXmlNodes(mockDocument);

        doAnswer(invocation -> {
            DOMSource source = invocation.getArgument(0);
            StreamResult result = invocation.getArgument(1);

            StringWriter writer = new StringWriter();
            writer.write("<Request>");
            writer.write("<Authentication>");
            writer.write("<Username>testUser</Username>");
            writer.write("<Password>testPass</Password>");
            writer.write("<Channel>testChannel</Channel>");
            writer.write("<Session>testSessionId</Session>");
            writer.write("</Authentication>");
            writer.write("<RequestParameters>");
            writer.write("<Insureds>");
            writer.write("<Insured ID=\"12345\">");
            writer.write("<Firstname>Tebele</Firstname>");
            writer.write("<Surname>Kalele</Surname>");
            writer.write("<Residency>RSA</Residency>");
            writer.write("<DOB>1999-10-19</DOB>");
            writer.write("</Insured>");
            writer.write("</Insureds>");
            writer.write("<TravelInformation>");
            writer.write("<StartDate>2025-01-01</StartDate>");
            writer.write("<EndDate>2025-02-02</EndDate>");
            writer.write("<DepartureCountry>RSA</DepartureCountry>");
            writer.write("<CoverCountries>");
            writer.write("<CoverCountry>LSE</CoverCountry>");
            writer.write("</CoverCountries>");
            writer.write("</TravelInformation>");
            writer.write("</RequestParameters>");
            writer.write("</Request>");

            result.getWriter().write(writer.toString());
            return null;
        }).when(mockTransformer).transform(any(DOMSource.class), any(StreamResult.class));

        Application app = new Application(mockDbFactory, mockDocBuilder, mockTransformerFactory, null);
        String requestMessage = app.getRequestMessage(mockInsured(), mockTravelInformation());

        assertNotNull(requestMessage);
        assertTrue(requestMessage.contains("<Username>testUser</Username>"));
        assertTrue(requestMessage.contains("<Password>testPass</Password>"));
        assertTrue(requestMessage.contains("<Channel>testChannel</Channel>"));
        assertTrue(requestMessage.contains("<Firstname>Tebele</Firstname>"));
        assertTrue(requestMessage.contains("<Surname>Kalele</Surname>"));
        assertTrue(requestMessage.contains("<Insured ID=\"12345\">"));
        assertTrue(requestMessage.contains("<DOB>1999-10-19</DOB>"));
        assertTrue(requestMessage.contains("<Residency>RSA</Residency>"));
        assertTrue(requestMessage.contains("<StartDate>2025-01-01</StartDate>"));
        assertTrue(requestMessage.contains("<EndDate>2025-02-02</EndDate>"));
        assertTrue(requestMessage.contains("<DepartureCountry>RSA</DepartureCountry>"));
        assertTrue(requestMessage.contains("<CoverCountry>LSE</CoverCountry>"));
        assertTrue(requestMessage.contains("<Session>testSessionId</Session>"));
    }

    private void mockXmlNodes(Document mockDocument) {
        Element mockRootElement = mock(Element.class);
        when(mockDocument.getDocumentElement()).thenReturn(mockRootElement);

        NodeList usernameNodeList = createMockNodeList("Username", "testUser");
        NodeList passwordNodeList = createMockNodeList("Password", "testPass");
        NodeList channelNodeList = createMockNodeList("Channel", "testChannel");
        NodeList firstnameNodeList = createMockNodeList("Firstname", "Tebele");
        NodeList surnameNodeList = createMockNodeList("Surname", "Kalele");
        NodeList dobNodeList = createMockNodeList("DOB", "1999-10-19");
        NodeList residencyNodeList = createMockNodeList("Residency", "RSA");
        NodeList startDateNodeList = createMockNodeList("StartDate", "2025-01-01");
        NodeList endDateNodeList = createMockNodeList("EndDate", "2025-12-31");
        NodeList departureCountryNodeList = createMockNodeList("DepartureCountry", "RSA");
        NodeList coverCountryNodeList = createMockNodeList("CoverCountry", "LSE");
        NodeList sessionNodeList = createMockNodeList("Session", "testSessionId");

        when(mockDocument.getElementsByTagName("Username")).thenReturn(usernameNodeList);
        when(mockDocument.getElementsByTagName("Password")).thenReturn(passwordNodeList);
        when(mockDocument.getElementsByTagName("Channel")).thenReturn(channelNodeList);
        when(mockDocument.getElementsByTagName("Firstname")).thenReturn(firstnameNodeList);
        when(mockDocument.getElementsByTagName("Surname")).thenReturn(surnameNodeList);
        when(mockDocument.getElementsByTagName("DOB")).thenReturn(dobNodeList);
        when(mockDocument.getElementsByTagName("Residency")).thenReturn(residencyNodeList);
        when(mockDocument.getElementsByTagName("StartDate")).thenReturn(startDateNodeList);
        when(mockDocument.getElementsByTagName("EndDate")).thenReturn(endDateNodeList);
        when(mockDocument.getElementsByTagName("DepartureCountry")).thenReturn(departureCountryNodeList);
        when(mockDocument.getElementsByTagName("CoverCountry")).thenReturn(coverCountryNodeList);
        when(mockDocument.getElementsByTagName("Session")).thenReturn(sessionNodeList);

        NodeList mockInsuredNodeList = mock(NodeList.class);
        Node mockInsuredNode = mock(Node.class);
        Element mockInsuredElement = mock(Element.class);

        when(mockDocument.getElementsByTagName("Insured")).thenReturn(mockInsuredNodeList);
        when(mockInsuredNodeList.getLength()).thenReturn(1);
        when(mockInsuredNodeList.item(0)).thenReturn(mockInsuredNode);
        when(mockInsuredNode.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(mockInsuredNodeList.item(0)).thenReturn(mockInsuredElement);
        when(mockInsuredElement.hasAttribute("ID")).thenReturn(true);
        when(mockInsuredElement.getAttribute("ID")).thenReturn("12345");
    }

    private NodeList createMockNodeList(String tagName, String textContent) {
        NodeList mockNodeList = mock(NodeList.class);
        Node mockNode = mock(Node.class);

        when(mockNodeList.getLength()).thenReturn(1);
        when(mockNodeList.item(0)).thenReturn(mockNode);
        when(mockNode.getNodeName()).thenReturn(tagName);
        when(mockNode.getTextContent()).thenReturn(textContent);

        return mockNodeList;
    }

    private Insured mockInsured() {
        Insured insured = mock(Insured.class);
        when(insured.getFirstname()).thenReturn("Tebele");
        when(insured.getSurname()).thenReturn("Kalele");
        when(insured.getId()).thenReturn("12345");
        when(insured.getDob()).thenReturn("1999-10-19");
        when(insured.getResidency()).thenReturn("RSA");
        return insured;
    }

    private TravelInformation mockTravelInformation() {
        TravelInformation travelInfo = mock(TravelInformation.class);
        when(travelInfo.getStartdate()).thenReturn("2025-01-01");
        when(travelInfo.getEnddate()).thenReturn("2025-12-31");
        when(travelInfo.getDeparturecountry()).thenReturn("RSA");
        when(travelInfo.getCovercountry()).thenReturn("LSE");
        return travelInfo;
    }
}
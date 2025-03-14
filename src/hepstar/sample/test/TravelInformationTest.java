package hepstar.sample.test;

import hepstar.sample.application.TravelInformation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TravelInformationTest {

    @Test
    public void testGettersAndSetters() {
        TravelInformation travelInformation = new TravelInformation("2025-03-01", "2025-03-10", "USA", "UK");

        assertEquals("2025-03-01", travelInformation.getStartdate());
        assertEquals("2025-03-10", travelInformation.getEnddate());
        assertEquals("USA", travelInformation.getDeparturecountry());
        assertEquals("UK", travelInformation.getCovercountry());

        travelInformation.setStartdate("2025-03-05");
        travelInformation.setEnddate("2025-03-15");
        travelInformation.setDeparturecountry("Canada");
        travelInformation.setCovercountry("France");

        assertEquals("2025-03-05", travelInformation.getStartdate());
        assertEquals("2025-03-15", travelInformation.getEnddate());
        assertEquals("Canada", travelInformation.getDeparturecountry());
        assertEquals("France", travelInformation.getCovercountry());
    }
}
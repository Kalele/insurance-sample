package hepstar.sample.test;

import hepstar.sample.application.Insured;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InsuredTest {

    @Test
    public void testDefaultConstructor() {
        Insured insured = new Insured();

        assertNull(insured.getId());
        assertNull(insured.getFirstname());
        assertNull(insured.getSurname());
        assertNull(insured.getDob());
        assertNull(insured.getResidency());
    }

    @Test
    public void testParameterizedConstructor() {
        Insured insured = new Insured("12345", "Tebele", "Kalele", "1999-19-10", "RSA");

        assertEquals("12345", insured.getId());
        assertEquals("Tebele", insured.getFirstname());
        assertEquals("Kalele", insured.getSurname());
        assertEquals("1999-19-10", insured.getDob());
        assertEquals("RSA", insured.getResidency());
    }

    @Test
    public void testGettersAndSetters() {
        Insured insured = new Insured();

        insured.setId("123451");
        insured.setFirstname("Tebele1");
        insured.setSurname("Kalele1");
        insured.setDob("1999-10-20");
        insured.setResidency("LES");

        assertEquals("123451", insured.getId());
        assertEquals("Tebele1", insured.getFirstname());
        assertEquals("Kalele1", insured.getSurname());
        assertEquals("1999-10-20", insured.getDob());
        assertEquals("LES", insured.getResidency());
    }

    @Test
    public void testSettersWithNullValues() {
        Insured insured = new Insured();

        insured.setId(null);
        insured.setFirstname(null);
        insured.setSurname(null);
        insured.setDob(null);
        insured.setResidency(null);

        assertNull(insured.getId());
        assertNull(insured.getFirstname());
        assertNull(insured.getSurname());
        assertNull(insured.getDob());
        assertNull(insured.getResidency());
    }
}
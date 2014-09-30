package entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jakobgaardandersen
 */
public class PersonTest {

    private Person p;

    public PersonTest() {
    }

    @Before
    public void setUp() {
        p = new Person("Leon", "Hansen", "29283838", "testemail@domain.com");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetFirstName() {
        String fname = p.getFirstName();
        assertEquals(fname, "Leon");
    }

    @Test
    public void testSetFirstName() {
        String fName = "Kurt";
        p.setFirstName(fName);
        assertEquals(p.getFirstName(), fName);
    }

    @Test
    public void testGetLastName() {
        String lname = p.getLastName();
        assertEquals(lname, "Hansen");
    }

    @Test
    public void testSetLastName() {
        String lName = "Simonsen";
        p.setLastName(lName);
        assertEquals(p.getLastName(), lName);
    }

}

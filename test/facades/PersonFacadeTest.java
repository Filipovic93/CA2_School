package facades;

import com.google.gson.Gson;
import entity.Person;
import entity.RoleSchool;
import entity.Teacher;
import exceptions.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jakobgaardandersen
 */
public class PersonFacadeTest {

    PersonFacade facade = PersonFacade.getFacade();
    Gson gson = new Gson();

    public PersonFacadeTest() {
    }

    @Before
    public void setUp() {
        facade.clearTablesForTesting();
        // clear the database before each test
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetPersonsAsJSON() {
        Person p = new Person("aaa", "aaa", "aaa", "aaa");
        Person person1 = facade.addPerson(gson.toJson(p));
        Person p2 = new Person("bbb", "bbb", "bbb", "bbb");
        Person person2 = facade.addPerson(gson.toJson(p2));

        //Make the Expected String
        Map<Integer, Person> test = new HashMap();
        test.put(person1.getId(), person1);
        test.put(person2.getId(), person2);
        String expected = gson.toJson(test.values());
        String result = facade.getPersonsAsJSON();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPersonAsJSON() throws Exception {
        testAddPerson();
    }

    @Test
    public void testAddPerson() throws Exception {
        Person person = facade.addPerson(gson.toJson(new Person("bbb", "bbb", "bbb", "bbb")));
        String expectedJsonString = gson.toJson(person);
        String actual = facade.getPersonAsJSON(person.getId());
        assertEquals(expectedJsonString, actual);
    }

    @Test
    public void testAddRole() throws Exception {
        Person person = facade.addPerson(gson.toJson(new Person("bbb", "bbb", "bbb", "bbb")));
        RoleSchool role = new Teacher("Physics");
        role.setRoleName("Teacher");
        facade.addRole(gson.toJson(role), person.getId());
        String personAsJSON = facade.getPersonAsJSON(person.getId());
        Person fromJson = gson.fromJson(personAsJSON, Person.class);
        RoleSchool getRole = fromJson.getRoles().get(0);
        assertEquals(role.getId(), getRole.getId());
        
    }

    @Test(expected = NotFoundException.class)
    public void testDelete() throws Exception {
        Person person = facade.addPerson(gson.toJson(new Person("bbb", "bbb", "bbb", "bbb")));
        facade.delete(person.getId());
        facade.getPersonAsJSON(person.getId());
    }

    @Test(expected = NotFoundException.class)
    public void testGetNonExistingPerson() throws Exception {
        facade.getPersonAsJSON(5);
    }
    
}

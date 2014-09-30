import com.google.gson.Gson;
import exceptions.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import entity.Person;
import entity.RoleSchool;
import entity.Teacher;
import facades.PersonMockFacade;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PersonFacadeTest {

    PersonMockFacade facade;
    Gson gson = new Gson();

    public PersonFacadeTest() {
    }

    @Before
    public void x() {
        //true will create a new facade instance for each test case
        facade = PersonMockFacade.getFacade(true);
    }

    @Test
    public void testAddPerson() throws NotFoundException {
        Person person = facade.addPerson(gson.toJson(new Person("bbb", "bbb", "bbb", "bbb")));
        String expectedJsonString = gson.toJson(person);
        String actual = facade.getPersonAsJSON(person.getId());
        assertEquals(expectedJsonString, actual);
    }

    @Test
    public void testGetPerson() throws Exception {
        testAddPerson();
    }

    @Test
    public void testGetPersons() {
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

    @Test(expected = NotFoundException.class)
    public void testDeletePerson() throws NotFoundException {
        Person person = facade.addPerson(gson.toJson(new Person("bbb", "bbb", "bbb", "bbb")));
        facade.delete(person.getId());
        facade.getPersonAsJSON(person.getId());

    }

    @Test(expected = NotFoundException.class)
    public void testGetNonExistingPerson() throws Exception {
        facade.getPersonAsJSON(5);
    }

    @Test
    public void testAddRole() throws NotFoundException {
        Person p = new Person("aaa", "bbb", "ccc", "ddd");
        RoleSchool role = new Teacher("Computer");
        role.setRoleName("Teacher");
        Person addPerson = facade.addPerson(gson.toJson(p));
        facade.addRole(gson.toJson(role), addPerson.getId());
        String personAsJSON = facade.getPersonAsJSON(p.getId());
        Person fromJson = gson.fromJson(personAsJSON, Person.class);
        assertEquals(role.getRoleName(), fromJson.getRoles().get(0).getRoleName());
        assertEquals(role.getId(), fromJson.getRoles().get(0).getId());
    }

}

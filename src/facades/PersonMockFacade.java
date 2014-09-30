package facades;

import com.google.gson.Gson;
import entity.Person;
import entity.RoleSchool;
import exceptions.NotFoundException;
import interfaces.IPersonFacade;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jakobgaardandersen
 */
public class PersonMockFacade implements IPersonFacade {

    Map<Integer, Person> persons = new HashMap();
    private int nextId;
    private final Gson gson = new Gson();
    private static PersonMockFacade instance = new PersonMockFacade();

    private PersonMockFacade() {
    }

    /*
     Pass in true to create a new instance. Usefull for testing.
     */
    public static PersonMockFacade getFacade(boolean reseet) {
        if (true) {
            instance = new PersonMockFacade();
        }
        return instance;
    }

    /*
     Only meant for testing during development
     */
    public void createTestData() {
        addPerson(gson.toJson(new Person("Lars", "Mortensen", "1234", "test@test.com")));
        addPerson(gson.toJson(new Person("John", "Handsen", "2345", "test@test.com")));
        addPerson(gson.toJson(new Person("Peter", "Olsen", "3456", "test@test.com")));
        addPerson(gson.toJson(new Person("John", "McDonald", "4567", "test@test.com")));
        addPerson(gson.toJson(new Person("George", "Peterson", "5678", "test@test.com")));
    }

    @Override
    public String getPersonsAsJSON() {
        if (persons.isEmpty()) {
            return null;
        }
        return gson.toJson(persons.values());
    }

    @Override
    public String getPersonAsJSON(int id) throws NotFoundException {
        Person p = persons.get(id);
        if (p == null) {
            throw new NotFoundException("No person exists for the given id");
        }
        return gson.toJson(p);
    }

    @Override
    public Person addPerson(String json) {
        Person p = gson.fromJson(json, Person.class);
        p.setId(nextId);
        persons.put(nextId, p);
        nextId++;
        return p;
    }

    @Override
    public RoleSchool addRole(String json, int id) throws NotFoundException {
        RoleSchool rs = gson.fromJson(json, RoleSchool.class);
        Person p = persons.get(id);
        if (p == null) {
            throw new NotFoundException("No person exists for the given id");
        }
        p.addRole(rs);
        return rs;
    }

    @Override
    public Person delete(int id) throws NotFoundException {
        Person p = persons.remove(id);
        if (p == null) {
            throw new NotFoundException("No person exists for the given id");
        }
        return p;
    }

}

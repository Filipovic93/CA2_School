package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jakobgaardandersen
 */
public class IntegrationTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("CA2-SchoolPU");
    EntityManager em = emf.createEntityManager();

    public IntegrationTest() {
    }

    @Before
    public void setUp() {
        em.getTransaction().begin();
        Query q3 = em.createNativeQuery("DELETE FROM PERSON");
        q3.executeUpdate();
        em.getTransaction().commit();
    }

    @After
    public void tearDown() {
        
    }

    @Test
    public void getCorrectPerson() {
        Person p = new Person("Leon", "Hansen", "23232323", "test@test.com");
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();

        Person find = em.find(Person.class, p.getId());
        assertEquals(find, p);
    }

    @Test
    public void getAllPerson() {
        Person p1 = new Person("Damjan", "Filipovic", "41288757", "Damii93@hotmail.com");
        Person p2 = new Person("Vuk", "Rajovic", "11111111", "messikrkic@hansmail.lort");
        Person p3 = new Person("Jakob", "Gaard Andersen", "22222222", "hyberrazor@hahaha.spil");
        Person p4 = new Person("Youssef", "Ahmed", "33333333", "VANDPIBE@SHISHA.dk");

        Collection<Person> persons = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        persons.add(p4);
        
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.persist(p4);
        em.getTransaction().commit();

        List<Person> resultList = em.createQuery("SELECT p FROM Person p").getResultList();
        Person[] resultArray = resultList.toArray(new Person[0]);
        Arrays.sort(resultArray);
        Person[] ps = persons.toArray(new Person[0]);
        assertArrayEquals(resultArray, ps);
        
    }

}

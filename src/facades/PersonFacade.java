/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Person;
import entity.RoleSchool;
import exceptions.NotFoundException;
import interfaces.IPersonFacade;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Neno
 */
public class PersonFacade implements IPersonFacade
{

    private static PersonFacade instance;

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final Gson gson;

    private PersonFacade()
    {
        this.emf = Persistence.createEntityManagerFactory("CA2-SchoolPU");
        this.em = emf.createEntityManager();
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    }

    public static PersonFacade getFacade()
    {
        if (instance == null)
        {
            instance = new PersonFacade();
        }
        return instance;
    }

    @Override
    public String getPersonsAsJSON()
    {
        Collection<Person> resultList = em.createQuery("SELECT p FROM Person p").getResultList();
        return gson.toJson(resultList);
    }

    @Override
    public String getPersonAsJSON(int id) throws NotFoundException
    {
        Person p = em.find(Person.class, id);
        if (p == null)
        {
            throw new NotFoundException("No person exists for the given id");
        }
        return gson.toJson(p);
    }

    @Override
    public Person addPerson(String json)
    {
        Person p = gson.fromJson(json, Person.class);
        em.getTransaction().begin();
        try
        {
            em.persist(p); // persist should put to database and automatically give an id
            em.getTransaction().commit();
        } catch (Exception e)
        {
            em.getTransaction().rollback();
            p = null;
        }
        return p;
    }

    @Override
    public RoleSchool addRole(String json, int id) throws NotFoundException
    {
        RoleSchool role = gson.fromJson(json, RoleSchool.class);
        Person p = em.find(Person.class, id);
        if (p == null)
        {
            throw new NotFoundException("No person exists for the given id");
        }
        em.getTransaction().begin();
        try
        {
            em.persist(role);
            role.setPerson(p);
            p.addRole(role);
            em.getTransaction().commit();
        } catch (Exception e)
        {
            em.getTransaction().rollback();
            role = null;
        }
        return role;
    }

    @Override
    public Person delete(int id) throws NotFoundException
    {
        Person p = em.find(Person.class, id);
        if (p == null)
        {
            throw new NotFoundException("No person exists for the given id");
        }
        em.getTransaction().begin();
        try
        {
            em.remove(p);
            em.getTransaction().commit();
        } catch (Exception e)
        {
            em.getTransaction().rollback();
            p = null;
        }
        return p;
    }
    
    public void clearTablesForTesting() {
        try {
            em.getTransaction().begin();
            Query q1 = em.createNativeQuery("DELETE FROM PERSON");
            Query q2 = em.createNativeQuery("DELETE FROM ROLESCHOOL");
            Query q3 = em.createNativeQuery("DELETE FROM STUDENT");
            Query q4 = em.createNativeQuery("DELETE FROM TEACHER");
            Query q5 = em.createNativeQuery("DELETE FROM ASSISTENTTEACHER");
            q1.executeUpdate();
            q2.executeUpdate();
            q3.executeUpdate();
            q4.executeUpdate();
            q5.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

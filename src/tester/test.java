/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import entity.Person;
import entity.RoleSchool;
import entity.Student;
import entity.Teacher;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import rest.WebService;

/**
 *
 * @author Filipovic
 */
public class test {
    
    private final String ip = "100.85.76.5";
    private final int port = 8080;

    public void tester() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CA2-SchoolPU");
        EntityManager em = emf.createEntityManager();

        Person p1 = new Person("Damjan", "Filipovic", "41288727", "Dami96@hotmail.com");
        Person p2 = new Person("Vuk", "Rajovic", "11111111", "messikrkic@hansmail.com");
        Person p3 = new Person("Jakob", "Gaard Andersen", "22222222", "hyberrazor@hahaha.com");
        Person p4 = new Person("Youssef", "Ahmed", "33333333", "VANDPIBE@SHISHA.dk");
        RoleSchool role1 = new Teacher("Physics");
        RoleSchool role2 = new Student("3rd semester");
        
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.persist(p4);
        em.persist(role2);
        em.persist(role1);
        role1.setRoleName("Teacher");
        role2.setRoleName("Student");
        p2.addRole(role1);
        p2.addRole(role2);
        em.getTransaction().commit();
        
    }
    
    public void startServer() throws IOException
    {
        new WebService(ip,port).start();
    }

    public static void main(String[] args) throws IOException {
        test test = new test();
        test.tester();
        test.startServer();
    }

}

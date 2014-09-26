/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tester;

import entity.Person;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Filipovic
 */
public class test {
    
    public void tester () {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("CA2-SchoolPU");
    EntityManager em = emf.createEntityManager();
   
     Person p1 = new Person ("Damjan", "Filipovic", "41288757", "Damii93@hotmail.com");
     Person p2 = new Person ("Vuk","Rajovic","11111111","messikrkic@hansmail.lort");
     Person p3 = new Person ("Jakob","Gaard Andersen","22222222","hyberrazor@hahaha.spil");
     Person p4 = new Person("Youssef", "Ahmed", "33333333", "VANDPIBE@SHISHA.dk");
     
     em.getTransaction().begin();
     em.persist(p1);
     em.persist(p2);
     em.persist(p3);
     em.persist(p4);
     em.getTransaction().commit();
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);
}
     
     
     public static void main(String[] args) {
       new test().tester();
    }
     
     
     
     
}

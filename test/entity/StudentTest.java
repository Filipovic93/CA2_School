/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Filipovic
 */
public class StudentTest {

    private Student s;

    public StudentTest() {
    }

    @Before
    public void setUp() {
        s = new Student("3. semester");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetSemester() {
        String sem = s.getSemester();
        assertEquals(sem, "3. semester");
    }

    @Test
    public void testSetSemester() {
        String sem = ("4. semester");
        s.setSemester(sem);
        assertEquals(s.getSemester(), sem);
    }

}

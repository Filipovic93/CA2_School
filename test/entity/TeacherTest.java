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
public class TeacherTest {

    private Teacher t;

    public TeacherTest() {
    }

    @Before
    public void setUp() {
        t = new Teacher("Philosophy");
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testGetDegree() {
        String deg = t.getDegree();
        assertEquals(deg, "Philosophy");
    }

    @Test
    public void testSetDegree() {
        String deg = ("Computer Science");
        t.setDegree(deg);
        assertEquals(deg, t.getDegree());
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author Filipovic
 */
@Entity
public class Person implements Serializable, Comparable<Person> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s1")
    @SequenceGenerator(name = "s1",
                       sequenceName = "PERSON_SEQ",
                       initialValue = 1,
                       allocationSize = 1)
    @Expose
    private int id;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String phone;
    @Expose
    private String email;

    @Expose
    @OneToMany
    @JoinColumn
    private final Collection<RoleSchool> roles = new ArrayList<>();

    public Person() {
    }

    public Person(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Person other) {
        return this.getId() - other.getId();
    }

    public void addRole(RoleSchool rs) {
        roles.add(rs);
    }

    public List<RoleSchool> getRoles() {
        return (List<RoleSchool>) roles;
    }

}

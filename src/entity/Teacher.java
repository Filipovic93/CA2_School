package entity;

import javax.persistence.Entity;

/**
 *
 * @author Youssefa
 */
@Entity

public class Teacher extends RoleSchool {

    private String degree;

    public Teacher() {

    }

    public Teacher(String degree) {
        this.degree = degree;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

}

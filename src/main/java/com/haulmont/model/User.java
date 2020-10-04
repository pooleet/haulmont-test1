package com.haulmont.model;

import java.io.Serializable;

public class User implements Serializable, Cloneable {

    private Long id;


    @Override
    public User clone() throws CloneNotSupportedException {
                return (User) super.clone();
    }

    private String firstName;
    private String lastName;
    private String fatherName;
    private Role role;

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public  String getFirstName() {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }




    @Override
    public String toString() {
        return id + " " + firstName + " " + lastName + " " + fatherName + " " + role ;
    }


}

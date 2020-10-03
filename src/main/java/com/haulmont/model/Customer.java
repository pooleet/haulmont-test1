package com.haulmont.model;

import java.io.Serializable;

public class Customer implements Serializable, Cloneable {
    private User name;
    private String phone;
    private int countOrder;

//

    public Customer() {
    }
    private String firstNameL;

    public String getFirstNameL() {
        return name.getFirstName();
    }
    public void setFirstNameL(String firstNameL) {
        this.firstNameL = firstNameL;
        name.setFirstName(firstNameL);
    }

    private String lastNameL;
    public String getLastNameL() {
        return name.getLastName();
    }
    public void setLastNameL(String lastNameL) {
        this.lastNameL = lastNameL;
        name.setLastName(lastNameL);
    }

    private String fatherNameL;
    public String getFatherNameL() {
        return name.getFatherName();
    }
    public void setFatherNameL(String fatherNameL) {
        this.fatherNameL = fatherNameL;
        name.setFatherName(fatherNameL);
    }



//


    public Customer(User name, String phone, int countOrder) {
        this.name = name;
        this.phone = phone;
        this.countOrder = countOrder;
    }

    public int getCountOrder() {
        return countOrder;
    }

    public void setCountOrder(int countOrder) {
        this.countOrder = countOrder;
    }

    public User getName() {
        return name;
    }

    public void setName(User name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {


        return name.getId() + " " + phone + " " + countOrder;
    }


    @Override
    public Customer clone() throws CloneNotSupportedException {

        Customer newCuctomer = (Customer) super.clone();
        newCuctomer.name = name.clone();
        return newCuctomer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.name.getId() == null) {
            return false;
        }

        if (obj instanceof Customer && obj.getClass().equals(getClass())) {
            return this.name.getId().equals(((Customer) obj).name.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (name.getId() == null ? 0 : name.getId().hashCode());
        return hash;
    }
}

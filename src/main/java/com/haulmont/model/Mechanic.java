package com.haulmont.model;

import java.io.Serializable;

public class Mechanic implements Serializable, Cloneable {
    private User name;
    private Double price;
    private int countOrder;

    public Mechanic(User name, Double price, int countOrder) {
        this.name = name;
        this.price = price;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name.toString() + " " + price + " " + countOrder;
    }



    @Override
    public Mechanic clone() throws CloneNotSupportedException {
        Mechanic newMechanic = (Mechanic) super.clone();
        newMechanic.name= name.clone();
        return newMechanic;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.name.getId() == null) {
            return false;
        }

        if (obj instanceof Mechanic && obj.getClass().equals(getClass())) {
            return this.name.getId().equals(((Mechanic) obj).name.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (name.getId() == null ? 0 : name.getId().hashCode());
        return hash;
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

    private String FatherNameL;
    public String getFatherNameL() {
        return name.getFatherName();
    }
    public void setFatherNameL(String fatherNameL) {
        FatherNameL = fatherNameL;
        name.setFatherName(fatherNameL);
    }


    private String priceL;
    public String getPriceL() {
        return price.toString();
    }

    public void setPriceL(String priceL) {
        setPrice(Double.parseDouble(priceL));
        this.priceL = priceL;
    }


}

package com.haulmont.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Order implements Serializable, Cloneable {
    private Long id;
    private Long idc;
    private String nameC;
    private Long idcM;
    private String nameM;
    private String description;
    private LocalDate dateStart;
    private LocalDate dateStop;
    private Double price;
    private WorkStatus workStatus;

    public Order() {
    }

    public Order(Long id, Long idc, String nameC, Long idcM, String nameM, String description, LocalDate dateStart, LocalDate dateStop, Double price, WorkStatus workStatus) {
        this.id = id;
        this.idc = idc;
        this.nameC = nameC;
        this.idcM = idcM;
        this.nameM = nameM;
        this.description = description;
        this.dateStart = dateStart;
        this.dateStop = dateStop;
        this.price = price;
        this.workStatus = workStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdc() {
        return idc;
    }

    public void setIdc(Long idc) {
        this.idc = idc;
    }

    public String getNameC() {
        return nameC;
    }

    public void setNameC(String nameC) {
        this.nameC = nameC;
    }

    public Long getIdcM() {
        return idcM;
    }

    public void setIdcM(Long idcM) {
        this.idcM = idcM;
    }

    public String getNameM() {
        return nameM;
    }

    public void setNameM(String nameM) {
        this.nameM = nameM;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = convertToLocalDateViaMilisecond(dateStart);
    }

    public LocalDate getDateStop() {
        return dateStop;
    }

    public void setDateStop(Date dateStop) {
        this.dateStop = convertToLocalDateViaMilisecond(dateStop);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public WorkStatus getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;
    }


    @Override
    public Order clone() throws CloneNotSupportedException {
        return (Order) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.id == null) {
            return false;
        }

        if (obj instanceof Order && obj.getClass().equals(getClass())) {
            return this.id.equals(((Order) obj).id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (id == null ? 0 : id.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        return
                id +
                        idc +
                        nameC +
                        idcM +
                        nameM +
                        description +
                        dateStart +

                        dateStop +
                        price +
                        workStatus
                ;
    }


    public LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {

        if (dateToConvert!=null){

        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();}
        else return null;
    }
}

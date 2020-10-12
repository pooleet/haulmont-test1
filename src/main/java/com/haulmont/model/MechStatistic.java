package com.haulmont.model;

import com.haulmont.controller.MechStatService;

import java.io.Serializable;
import java.text.DecimalFormat;

public class MechStatistic  implements Serializable, Cloneable {
    public Long id;
    public String name;
    public Long countWork;
    public Long countNotWork;
    public Double countMoney;
    public Double countTime;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCountWork() {
        return countWork;
    }

    public void setCountWork(Long countWork) {
        this.countWork = countWork;
    }

    public Long getCountNotWork() {
        return countNotWork;
    }

    public void setCountNotWork(Long countNotWork) {
        this.countNotWork = countNotWork;
    }

    public String getCountMoney() {
        return countMoney.toString();
    }

    public void setCountMoney(Double countMoney) {
        this.countMoney = countMoney;
    }

    public String getCountTime() {
        DecimalFormat decimalFormat = new DecimalFormat( "#.##" );
        return  decimalFormat.format(countTime);
    }

    public void setCountTime(Double countTime) {

        this.countTime = countTime;
    }


    public MechStatistic() {
    }

    @Override
    public MechStatistic clone() throws CloneNotSupportedException {
        return (MechStatistic) super.clone();
    }

    @Override
    public String toString() {
        return
                 id + name + countWork +countNotWork +countMoney +countTime;
    }
}

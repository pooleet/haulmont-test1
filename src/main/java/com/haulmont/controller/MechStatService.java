package com.haulmont.controller;

import com.haulmont.model.MechStatistic;
import com.haulmont.model.Order;
import com.haulmont.model.User;
import db.SqlMechanic;
import db.SqlOrder;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MechStatService {
    private static MechStatService instance;
    private final HashMap<Long, MechStatistic> mechStatistic = new HashMap<>();

    private SqlMechanic sql = new SqlMechanic();


    public static MechStatService getInstance() throws SQLException, ClassNotFoundException {

        instance = new MechStatService();
        instance.loadData();

        return instance;
    }

    public synchronized List<MechStatistic> findAll() {
        return findAll(null);
    }

    public void loadData() throws SQLException, ClassNotFoundException {

        //обънкт пустой?
        mechStatistic.clear();
        if (findAll().isEmpty()) {

            ArrayList<MechStatistic> mechList = sql.getStatistic();
            for (MechStatistic s : mechList) {
                // System.out.println("id   "+s.getName().getId());
                save(s);
            }


        }

    }

    public synchronized void save(MechStatistic entry) {

        try {
            entry = entry.clone();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        mechStatistic.put(entry.getId(), entry);
     //   System.out.println("Статистика  " + mechStatistic.size());

    }

    public synchronized List<MechStatistic> findAll(String stringFilter) {

        ArrayList<MechStatistic> arrayList = new ArrayList<>();
        // получить набор из всех значений
        for (MechStatistic user : mechStatistic.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || user.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(user.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<MechStatistic>() {

            @Override
            public int compare(MechStatistic o1, MechStatistic o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }
}

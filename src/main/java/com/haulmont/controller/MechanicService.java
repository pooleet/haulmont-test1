package com.haulmont.controller;


import com.haulmont.model.Customer;
import com.haulmont.model.Mechanic;
import db.SqlMechanic;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MechanicService {

    private static MechanicService instance;
    private final HashMap<Long, Mechanic> mechanics = new HashMap<>();
    private SqlMechanic sql = new SqlMechanic();


    // запомним максимальны индекс
    private long nextId = 0;


    // проверяем наличие оюбъектов
    public static MechanicService getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new MechanicService();
            instance.loadData();
        }
        return instance;
    }

    // дает возмодность выполнять код только 1 потоком
    public synchronized List<Mechanic> findAll() {
        return findAll(null);
    }


    public void loadData() throws SQLException, ClassNotFoundException {

        //обънкт пустой?
        mechanics.clear();
        if (findAll().isEmpty()) {

            ArrayList<Mechanic> mechanic = sql.loadMechanicList();
            for (Mechanic s : mechanic) {
               // System.out.println("id   "+s.getName().getId());
                save(s);
            }


        }

    }


    public synchronized void delete(Mechanic value) {

        mechanics.remove(value.getName().getId());
        // добавить метод удаления из бд
    }


    public synchronized void save(Mechanic entry) {

        try {
            entry = entry.clone();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        mechanics.put(entry.getName().getId(), entry);
        System.out.println("Механик  " + mechanics.size());

    }

    // фильтер
    public synchronized List<Mechanic> findAll(String stringFilter) {

        ArrayList<Mechanic> arrayList = new ArrayList<>();
        // получить набор из всех значений
        for (Mechanic mechanic : mechanics.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || mechanic.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(mechanic.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MechanicService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Mechanic>() {

            @Override
            public int compare(Mechanic o1, Mechanic o2) {
                return (int) (o2.getName().getId() - o1.getName().getId());
            }
        });
        return arrayList;
    }
}

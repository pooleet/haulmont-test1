package com.haulmont.controller;

import com.haulmont.model.User;
import com.haulmont.model.User;
import db.SqlMechanic;
import db.SqlUser;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {

    private static UserService instance;
    private final HashMap<Long, User> users = new HashMap<>();
    private SqlUser sql = new SqlUser();

    // проверяем наличие оюбъектов
    public static UserService getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new UserService();
            instance.loadData();
        }
        return instance;
    }

    // дает возмодность выполнять код только 1 потоком
    public synchronized List<User> findAll() {
        return findAll(null);
    }

    public void loadData() throws SQLException, ClassNotFoundException {

        //обънкт пустой?
        users.clear();
        if (findAll().isEmpty()) {

            ArrayList<User> users = sql.loadUserList();
            for (User s : users) {
                // System.out.println("id   "+s.getName().getId());
                save(s);
            }


        }

    }


    public synchronized void delete(User value) {

        users.remove(value.getId());
        // добавить метод удаления из бд
    }


    public synchronized void save(User entry) {

        try {
            entry = entry.clone();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        users.put(entry.getId(), entry);
        System.out.println("Человек " + users.size());

    }

    // фильтер
    public synchronized List<User> findAll(String stringFilter) {

        ArrayList<User> arrayList = new ArrayList<>();
        // получить набор из всех значений
        for (User user : users.values()) {
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
        Collections.sort(arrayList, new Comparator<User>() {

            @Override
            public int compare(User o1, User o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }


}

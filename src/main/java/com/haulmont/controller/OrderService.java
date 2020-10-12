package com.haulmont.controller;


import com.haulmont.model.Order;
import db.SqlOrder;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderService {

    private static OrderService instance;
    private final HashMap<Long, Order> orders = new HashMap<>();

    private SqlOrder sql = new SqlOrder();

    // запомним максимальны индекс
    private long nextId = 0;

    // проверяем наличие оюбъектов
    public static OrderService getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new OrderService();
            instance.loadData();
        }
        return instance;
    }

    public void loadData() throws SQLException, ClassNotFoundException {

        //объект пустой?
        if (!findAll().isEmpty()) {
            orders.clear();
        }

        if (findAll().isEmpty()) {

            ArrayList<Order> order = sql.loadOrderList();
            for (Order s : order) {
                //System.out.println("id ОРДЕР   " + s.getId());
                save(s);
            }
        }


    }

    // дает возмодность выполнять код только 1 потоком
    public synchronized List<Order> findAll() {
        return findAll(null, null, null);
    }

    // фильтер
    public synchronized List<Order> findAll(String name, String stat, String descr) {

        ArrayList<Order> arrayList = new ArrayList<>();
        String[] arr = new String[]{name, stat, descr};

        // получить набор из всех значений


        for (Order order : orders.values()) {
            boolean passesFilter1 = false;
            boolean passesFilter2 = false;
            boolean passesFilter3 = false;
            try {
                for (int i = 0; i < arr.length; i++) {
                    if (i == 0) {
                        // String fname=order.getNameC();
                        passesFilter1 = (arr[i] == null || arr[i].isEmpty())
                                || order.getNameC().toLowerCase().contains(arr[i].toLowerCase());
                        i = +i;
                    }

                    if (i == 1) {

                        passesFilter2 = (arr[i] == null || arr[i].isEmpty())
                                || order.getWorkStatus().name().toLowerCase().contains(arr[i].toLowerCase());
                        i = +i;
                    }

                    if (i == 2) {

                        passesFilter3 = (arr[i] == null || arr[i].isEmpty())
                                || order.getDescription().toLowerCase().contains(arr[i].toLowerCase());
                        i = +i;
                    }
                }



                if (passesFilter1 && passesFilter2 && passesFilter3 && (!arrayList.contains(order))) {
                    arrayList.add(order.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(OrderService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Order>() {

            @Override
            public int compare(Order o1, Order o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });

        return arrayList;
    }

    public synchronized void save(Order entry) {

        try {
            entry = entry.clone();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        orders.put(entry.getId(), entry);
       // System.out.println("Заказ  " + orders.size());

    }

    public synchronized void delete(Order value) {

        orders.remove(value.getId());
        // добавить метод удаления из бд
    }


}

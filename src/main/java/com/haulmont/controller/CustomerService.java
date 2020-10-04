package com.haulmont.controller;

import com.haulmont.model.Customer;
import db.SqlCustomer;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerService {

    private static CustomerService instance;
    private final HashMap<Long, Customer> contacts = new HashMap<>();
    private SqlCustomer sql = new SqlCustomer();

    // запомним максимальны индекс
    private long nextId = 0;


    public CustomerService() {

    }

    // проверяем наличие оюбъектов
    public static CustomerService getInstance() throws SQLException{
        if (instance == null) {
            instance = new CustomerService();
            instance.loadData();

        }
        return instance;
    }

    // дает возмодность выполнять код только 1 потоком
    public synchronized List<Customer> findAll() {
        return findAll(null);
    }


    // заполняем таблицу данными
    public void loadData() throws SQLException {

         //обънкт пустой?
        contacts.clear();
        if (findAll().isEmpty()) {

            ArrayList<Customer> cucstomer = sql.loadCustomerList();
            for (Customer s : cucstomer) {
               // System.out.println("id   "+s.getName().getId());
                save(s);
            }
        }
    }


    public synchronized void delete(Customer value) {
        contacts.remove(value.getName().getId());
        // добавить метод удаления из бд
    }


    public synchronized void save(Customer entry) {
         /* if (entry == null) {
          LOGGER.log(Level.SEVERE,
                    "Customer is null. Are you sure you have connected your form to the application as described in tutorial chapter 7?");
            return;
        }
            if (entry.getName().getId() == null) {
                //  entry.setId(nextId++);   //Поменять

            }*/
            try {

                entry = (Customer) entry.clone();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            contacts.put(entry.getName().getId(), entry);
           // System.out.println(entry.getName().getFirstName());

    }

    // фильтер
    public synchronized List<Customer> findAll(String stringFilter) {

        ArrayList<Customer> arrayList = new ArrayList<>();
        // получить набор из всех значений
        for (Customer contact : contacts.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Customer>() {

            @Override
            public int compare(Customer o1, Customer o2) {
                return (int) (o2.getName().getId() - o1.getName().getId());
            }
        });
        return arrayList;
    }
}

package com.haulmont.controller;

import com.haulmont.model.Cust;
import com.haulmont.model.CustomerStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An in memory dummy "database" for the example purposes. In a typical Java app
 * this class would be replaced by e.g. EJB or a Spring based service class.
 * <p>
 * In demos/tutorials/examples, get a reference to this service class with
 * {@link CustService#getInstance()}.
 */
public class CustService {

    private static CustService instance;
    private static final Logger LOGGER = Logger.getLogger(CustService.class.getName());

    private final HashMap<Long, Cust> contacts = new HashMap<>();
    private long nextId = 0;

    private CustService() {
    }

    /**
     * @return a reference to an example facade for Customer objects.
     */
    public static CustService getInstance() {
        if (instance == null) {
            instance = new CustService();
            instance.ensureTestData();
        }
        return instance;
    }

    /**
     * @return all available Customer objects.
     */
    public synchronized List<Cust> findAll() {
        return findAll(null);
    }

    /**
     * Finds all Customer's that match given filter.
     *
     * @param stringFilter filter that returned objects should match or null/empty string
     *                     if all objects should be returned.
     * @return list a Customer objects
     */
    public synchronized List<Cust> findAll(String stringFilter) {
        ArrayList<Cust> arrayList = new ArrayList<>();
        // получить набор из всех значений
        for (Cust contact : contacts.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(CustService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Cust>() {

            @Override
            public int compare(Cust o1, Cust o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }

    /**
     * Finds all Customer's that match given filter and limits the resultset.
     *
     * @param stringFilter filter that returned objects should match or null/empty string
     *                     if all objects should be returned.
     * @param start        the index of first result
     * @param maxresults   maximum result count
     * @return list a Customer objects
     */
    public synchronized List<Cust> findAll(String stringFilter, int start, int maxresults) {
        ArrayList<Cust> arrayList = new ArrayList<>();
        for (Cust contact : contacts.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(CustService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Cust>() {

            @Override
            public int compare(Cust o1, Cust o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        int end = start + maxresults;
        if (end > arrayList.size()) {
            end = arrayList.size();
        }
        return arrayList.subList(start, end);
    }

    /**
     * @return the amount of all customers in the system
     */
    public synchronized long count() {
        return contacts.size();
    }

    /**
     * Deletes a customer from a system
     *
     * @param value the Customer to be deleted
     */
    public synchronized void delete(Cust value) {
        contacts.remove(value.getId());
    }

    /**
     * Persists or updates customer in the system. Also assigns an identifier
     * for new Customer instances.
     *
     * @param entry
     */
    public synchronized void save(Cust entry) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE,
                    "Customer is null. Are you sure you have connected your form to the application as described in tutorial chapter 7?");
            return;
        }
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Cust) entry.clone();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        contacts.put(entry.getId(), entry);
    }

    /**
     * Sample data generation
     */
    public void ensureTestData() {
        if (findAll().isEmpty()) {
            final String[] names = new String[]{"Gabrielle Patel", "Brian Robinson", "Eduardo Haugen",
                    "Koen Johansen", "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Ha  en Svensson",
                    "Emily Stewart", "Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson",
                    "Eileen Walker", "Katelyn Martin", "Israel Carlsson", "Quinn Hansson", "Makena Smith",
                    "Danielle Watson", "Leland Harris", "Gunner Karlsen", "Jamar Olsson", "Lara Martin",
                    "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen", "Solomon Olsen",
                    "Jaydan Jackson", "Bernard Nilsen"};
            Random r = new Random(0);
            for (String name : names) {
                String[] split = name.split(" ");
                Cust c = new Cust();
                c.setFirstName(split[0]);
                c.setLastName(split[1]);
                c.setEmail(split[0].toLowerCase() + "@" + split[1].toLowerCase() + ".com");
                c.setStatus(CustomerStatus.values()[r.nextInt(CustomerStatus.values().length)]);
                int daysOld = 0 - r.nextInt(365 * 15 + 365 * 60);
                c.setBirthDate(LocalDate.now().plusDays(daysOld));
                save(c);
            }
        }
    }

}
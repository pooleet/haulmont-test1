package com.haulmont.controller;

import com.haulmont.model.Customer;
import com.haulmont.model.Order;
import db.SqlCustomer;
import db.SqlOrder;

import java.util.HashMap;

public class OrderService {

    private static OrderService instance;
    private final HashMap<Long, Order> contacts = new HashMap<>();
    private SqlOrder sql = new SqlOrder();

    // запомним максимальны индекс
    private long nextId = 0;
}

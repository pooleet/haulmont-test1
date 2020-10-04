package com.haulmont.viev;

import com.haulmont.MyUIO;
import com.haulmont.controller.OrderService;
import com.haulmont.controller.UserService;
import com.haulmont.model.Order;
import com.haulmont.model.User;
import com.haulmont.model.WorkStatus;
import com.vaadin.data.Binder;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import db.SqlOrder;
import db.SqlUser;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class MySubOrder extends Window {
    // поле общее окно Сюда все складываем.
    final VerticalLayout layoutWindowVertical = new VerticalLayout();
    // поле с кнопками
    final HorizontalLayout buttons = new HorizontalLayout();
    //окно с VerticalR VerticalL
    final HorizontalLayout HorizontalL = new HorizontalLayout();
    // правое окно
    final VerticalLayout VerticalR = new VerticalLayout();
    // левое окно
    final VerticalLayout VerticalL = new VerticalLayout();
    final RadioButtonGroup<WorkStatus> statusSingle =
            new RadioButtonGroup<>("Статус задания");
    private MyUIO myUIO;
    private Order order;
    private Binder<Order> binderOrder = new Binder<>(Order.class, true);
    // поле ввода задиния
    private TextField description = new TextField("Задание");
    private TextField filter = new TextField("Фильтр");
    private Grid<User> orderGrid = new Grid<>(User.class);

    private TextField price = new TextField("Цена работы");
    // Create a text field
    private DateField dateStart = new DateField("Создан");
    private DateField dateStop = new DateField("Выполнен");

    private Label TextCustomer = new Label("Заказчик:");
    private Label TextMechanic = new Label("Механик:");

    private Button save;
    private Button close = new Button("Закрыть");


/////
    private  Button popupButton = new Button("Open popup with MyPopupUI");

    private OrderService service;
    private UserService serviceU;
    private SqlOrder sql;
    private SqlUser sqlU;

    public MySubOrder(MyUIO components, String caption) {

        super(caption);
        setSizeUndefined();
        this.myUIO = components;
        save = new Button(caption);
        updateListUser();
        visual();
    }

    public void setOrder(Order order) {
        this.order = order;
        binderOrder.setBean(order);

       // dateStart.setValue(order.getDateStart());
    }


    // хребет окна
    private void visual() {

        buttons.addComponent(close);
        buttons.addComponent(save);

        LocalDate today = LocalDate.now();
        // создан

        // выполнен
 //   System.out.println(order.getDateStart()+"   "+dateStart);
       //if(order.getDateStart().) {
        dateStart.setValue(today);
        dateStop.setValue(null);
       // }
        dateStop.setDateFormat("yyyy-MM-dd");
        dateStart.setDateFormat("yyyy-MM-dd");
        statusSingle.setItems(WorkStatus.values());
        if (dateStop.getValue() == null) {
            statusSingle.setValue(WorkStatus.Запланирован);
        }
        if (dateStop.getValue() != null) {
            if (order.getWorkStatus().equals("Выполнен")) {
                statusSingle.setValue(WorkStatus.Выполнен);
            }
            if (order.getWorkStatus().equals("Принят_клиентом")) {
                statusSingle.setValue(WorkStatus.Принят_клиентом);
            }
        }
        //  single.setItems(WorkStatus.values().toString());
        VerticalL.addComponent(filter);

        filter.setPlaceholder("filter by name...");
        filter.addValueChangeListener(e -> updateListUser());
        filter.setValueChangeMode(ValueChangeMode.LAZY);


        VerticalL.addComponent(orderGrid);
        VerticalL.addComponent(statusSingle);
        VerticalL.addComponent(price);

        VerticalR.addComponents(dateStart);
        VerticalR.addComponents(dateStop);
        VerticalR.addComponents(TextCustomer);
        VerticalR.addComponents(TextMechanic);

        HorizontalL.addComponent(VerticalL);
        HorizontalL.addComponent(VerticalR);

        layoutWindowVertical.addComponent(description);
        layoutWindowVertical.addComponent(HorizontalL);
        layoutWindowVertical.addComponent(buttons);


        setContent(layoutWindowVertical);

        // закрыть

    }



    private void updateListUser() {

        try {
            serviceU = UserService.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<User> user = serviceU.findAll(filter.getValue());

        orderGrid.setItems(user);
    }


    public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }
}
